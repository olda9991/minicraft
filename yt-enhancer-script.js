/**
 * Enhancer for YouTube™ — Custom Script Pack
 * Safe DOM-only modifications. No external requests.
 * Copy ONE block at a time into the "Custom script" field.
 */

// ============================================================
// OPTION 1: Hide YouTube Shorts everywhere (homepage, sidebar, subscriptions)
// ============================================================
(function hideShorts() {
    const STYLE_ID = 'mc-hide-shorts';
    if (document.getElementById(STYLE_ID)) return;
    const style = document.createElement('style');
    style.id = STYLE_ID;
    style.textContent = `
        /* Shelf on homepage */
        ytd-rich-section-renderer:has([title="Shorts"]),
        ytd-reel-shelf-renderer,
        ytd-rich-shelf-renderer:has(a[href^="/shorts/"]),
        /* Sidebar Shorts link */
        ytd-guide-entry-renderer:has(a[title="Shorts"]),
        ytd-mini-guide-entry-renderer:has(a[title="Shorts"]),
        /* Search results */
        ytd-video-renderer:has(a[href^="/shorts/"]),
        ytd-reel-video-renderer,
        /* Subscription feed */
        ytd-grid-video-renderer:has(a[href^="/shorts/"]),
        ytd-rich-item-renderer:has(a[href^="/shorts/"]) {
            display: none !important;
        }
    `;
    document.head.appendChild(style);
    console.log('[MiniCraft Script] Shorts hidden');
})();


// ============================================================
// OPTION 2: Auto-click "Skip" ads + hide banner ads
// ============================================================
(function adSkip() {
    setInterval(() => {
        // Skip button (both old and new YT UI)
        const skipBtn = document.querySelector('.ytp-skip-ad-button, .ytp-ad-skip-button, button[class*="skip"]');
        if (skipBtn && skipBtn.offsetParent !== null) skipBtn.click();

        // Close overlay ads
        const closeAd = document.querySelector('.ytp-ad-overlay-close-button, .ytp-ad-close-button');
        if (closeAd) closeAd.click();

        // Hide "Info cards" teasers
        const infoCard = document.querySelector('.ytp-ce-element, .ytp-cards-button');
        if (infoCard) infoCard.style.display = 'none';
    }, 800);
})();


// ============================================================
// OPTION 3: Hide clutter below video (Thanks, Clip, Shop, Join)
// ============================================================
(function hideClutter() {
    const STYLE_ID = 'mc-hide-clutter';
    if (document.getElementById(STYLE_ID)) return;
    const style = document.createElement('style');
    style.id = STYLE_ID;
    style.textContent = `
        #actions ytd-button-renderer:has(button[aria-label*="Thanks"]),
        #actions ytd-button-renderer:has(button[aria-label*="Clip"]),
        #actions ytd-button-renderer:has(button[aria-label*="Shop"]),
        #actions ytd-button-renderer:has(button[aria-label*="Join"]),
        #actions ytd-button-renderer:has(button[aria-label*="Download"]),
        ytd-merch-shelf-renderer,
        ytd-product-list-renderer {
            display: none !important;
        }
    `;
    document.head.appendChild(style);
})();


// ============================================================
// OPTION 4: Auto-Theater mode on every video page load
// ============================================================
(function autoTheater() {
    const go = () => {
        const btn = document.querySelector('.ytp-size-button, button[data-title-key="theater"]');
        const body = document.body;
        if (btn && !body.classList.contains('theater-mode') && !body.classList.contains('persistent-theater')) {
            btn.click();
        }
    };
    if (location.pathname.startsWith('/watch')) {
        go();
        setTimeout(go, 2000); // retry after player settles
    }
})();


// ============================================================
// OPTION 5: Hide live chat + end-screen cards
// ============================================================
(function hideChatCards() {
    const STYLE_ID = 'mc-hide-chat-cards';
    if (document.getElementById(STYLE_ID)) return;
    const style = document.createElement('style');
    style.id = STYLE_ID;
    style.textContent = `
        ytd-live-chat-frame,
        #chat,
        .ytp-ce-element,
        .ytp-endscreen-content,
        ytd-item-section-renderer:has(#content:empty) {
            display: none !important;
        }
    `;
    document.head.appendChild(style);
})();


// ============================================================
// OPTION 6: Compact subscriptions feed (no thumbnails, list view)
// ============================================================
(function compactSubs() {
    if (!location.pathname.startsWith('/feed/subscriptions')) return;
    const STYLE_ID = 'mc-compact-subs';
    if (document.getElementById(STYLE_ID)) return;
    const style = document.createElement('style');
    style.id = STYLE_ID;
    style.textContent = `
        ytd-thumbnail, #thumbnail, .ytd-thumbnail { display: none !important; }
        ytd-grid-video-renderer, ytd-rich-item-renderer {
            display: flex !important; align-items: center !important; gap: 12px !important;
            padding: 4px 0 !important; border-bottom: 1px solid #333 !important;
        }
        #video-title { font-size: 14px !important; }
        #metadata-line { font-size: 11px !important; }
    `;
    document.head.appendChild(style);
})();

/* ============================================================
   HOW TO USE:
   1. Open Enhancer for YouTube™ settings → "Custom script"
   2. Delete the placeholder "mmmmmmmm" text
   3. Paste the blocks you want (delete the ones you don't want)
   4. Save & reload YouTube
   ============================================================ */
