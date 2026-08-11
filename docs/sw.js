/**
 * MiniCraft Web Service Worker
 * Network-first for game files, cache-first for CheerpJ runtime.
 * Never caches 404 / failed responses.
 */
const VERSION = '6.4.2-fatjar-v2';
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
        caches.open(CACHE_NAME).then(cache => {
            return Promise.all(OWN_FILES.map(url =>
                fetch(url).then(r => {
                    if (r.ok) return cache.put(url, r);
                    else console.warn('[SW] skip bad response', url, r.status);
                }).catch(err => console.warn('[SW] skip failed fetch', url, err))
            ));
        }).then(() => self.skipWaiting())
    );
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
        e.respondWith(
            caches.open(RUNTIME_CACHE).then(cache =>
                cache.match(e.request).then(resp => {
                    if (resp) return resp;
                    return fetch(e.request).then(r => {
                        if (r.ok) cache.put(e.request, r.clone());
                        return r;
                    });
                })
            )
        );
        return;
    }

    // Own files: network-first, only cache 200 OK
    e.respondWith(
        fetch(e.request).then(r => {
            if (r.ok) {
                const copy = r.clone();
                caches.open(CACHE_NAME).then(c => c.put(e.request, copy));
            }
            return r;
        }).catch(() => caches.match(e.request))
    );
});

self.addEventListener('message', e => {
    if (e.data === 'skipWaiting') self.skipWaiting();
});
