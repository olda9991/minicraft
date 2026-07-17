/**
 * MiniCraft Web Service Worker
 * Network-first for game files (instant updates), cache-first for CheerpJ runtime.
 */
const VERSION = '6.4.2';
const CACHE_NAME = 'minicraft-' + VERSION;
const RUNTIME_CACHE = 'minicraft-runtime';

const OWN_FILES = [
    './',
    './index.html',
    './MiniCraft.jar',
    './steve.png',
    './MINICRAFT.png',
    './discord.png',
    './github.png',
    './PixelPurl.ttf',
    './sw.js'
];

self.addEventListener('install', e => {
    e.waitUntil(
        caches.open(CACHE_NAME).then(cache => cache.addAll(OWN_FILES))
    );
    self.skipWaiting();
});

self.addEventListener('activate', e => {
    e.waitUntil(
        caches.keys().then(names =>
            Promise.all(names.map(n => {
                if (n !== CACHE_NAME && n !== RUNTIME_CACHE) return caches.delete(n);
            }))
        ).then(() => self.clients.claim())
    );
});

self.addEventListener('fetch', e => {
    const url = new URL(e.request.url);
    const isOwn = url.origin === self.location.origin;
    const isCheerpJ = url.hostname.includes('leaningtech.com');

    if (!isOwn && !isCheerpJ) {
        e.respondWith(fetch(e.request));
        return;
    }

    if (isCheerpJ) {
        // CheerpJ runtime: cache-first, never expires
        e.respondWith(
            caches.open(RUNTIME_CACHE).then(cache =>
                cache.match(e.request).then(resp => {
                    if (resp) return resp;
                    return fetch(e.request).then(r => {
                        cache.put(e.request, r.clone());
                        return r;
                    });
                })
            )
        );
        return;
    }

    // Our own files: network-first so updates are immediate
    e.respondWith(
        fetch(e.request).then(r => {
            const copy = r.clone();
            caches.open(CACHE_NAME).then(c => c.put(e.request, copy));
            return r;
        }).catch(() => {
            return caches.match(e.request);
        })
    );
});

// Listen for messages from page (e.g. "skipWaiting")
self.addEventListener('message', e => {
    if (e.data === 'skipWaiting') self.skipWaiting();
});
