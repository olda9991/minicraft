/**
 * MiniCraft Web Service Worker
 * No caching of local files. Only CheerpJ runtime is cached.
 */
const VERSION = '6.4.2-website';
const RUNTIME_CACHE = 'minicraft-runtime-v2';

self.addEventListener('install', e => {
    self.skipWaiting();
});

self.addEventListener('activate', e => {
    e.waitUntil(
        caches.keys().then(names =>
            Promise.all(names.map(n => {
                if (n !== RUNTIME_CACHE) return caches.delete(n);
            }))
        ).then(() => self.clients.claim())
    );
});

self.addEventListener('fetch', e => {
    const url = new URL(e.request.url);
    const isCheerpJ = url.hostname.includes('leaningtech.com');
    if (!isCheerpJ) return;
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
});
