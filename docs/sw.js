const CACHE_NAME = 'minicraft-v6.4.2';
const FILES = [
    './',
    './index.html',
    './MiniCraft.jar',
    './steve.png',
    './MINICRAFT.png',
    './discord.png',
    './github.png',
    './PixelPurl.ttf',
    'https://cjrtnc.leaningtech.com/3.0/cj3loader.js'
];

self.addEventListener('install', e => {
    e.waitUntil(
        caches.open(CACHE_NAME).then(cache => cache.addAll(FILES))
    );
    self.skipWaiting();
});

self.addEventListener('activate', e => {
    e.waitUntil(self.clients.claim());
});

self.addEventListener('fetch', e => {
    e.respondWith(
        caches.match(e.request).then(resp => {
            if (resp) return resp;
            return fetch(e.request).then(r => {
                // Cache CheerpJ runtime chunks for future visits
                if (e.request.url.includes('leaningtech.com')) {
                    const copy = r.clone();
                    caches.open(CACHE_NAME).then(c => c.put(e.request, copy));
                }
                return r;
            });
        })
    );
});
