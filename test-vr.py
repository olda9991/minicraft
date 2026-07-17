#!/usr/bin/env python3
"""
MiniCraft VR SBS Test Script
Generates multiple NO-HEADSET 3D viewing formats from a single SBS screenshot.
"""
import subprocess, time, os, sys
from PIL import Image

MC_DIR = os.path.expanduser("~/minicraft")
JAR = os.path.join(MC_DIR, "MiniCraft.jar")
OUT_DIR = os.path.join(MC_DIR, "vr-test-output")
os.makedirs(OUT_DIR, exist_ok=True)

def find_mc_window():
    try:
        out = subprocess.check_output(["xdotool", "search", "--name", "MiniCraft"], text=True)
        return out.strip().split("\n")[0]
    except Exception:
        return None

def is_mc_running():
    try:
        subprocess.check_output(["pgrep", "-f", "MiniCraft.jar"])
        return True
    except Exception:
        return False

def start_mc():
    print("[VR Test] Starting MiniCraft...")
    env = os.environ.copy()
    env["_JAVA_AWT_WM_NONREPARENTING"] = "1"
    env["DISPLAY"] = ":1"
    subprocess.Popen(
        ["java", "-jar", JAR],
        cwd=MC_DIR,
        env=env,
        stdout=subprocess.DEVNULL,
        stderr=subprocess.DEVNULL,
    )
    time.sleep(4)

def send_key(key):
    wid = find_mc_window()
    if not wid:
        print("[VR Test] ERROR: MiniCraft window not found!")
        sys.exit(1)
    subprocess.run(["xdotool", "windowfocus", wid], check=False)
    time.sleep(0.2)
    subprocess.run(["xdotool", "key", key], check=False)
    time.sleep(0.5)

def screenshot():
    path = os.path.join(OUT_DIR, "raw.png")
    subprocess.run(["grim", path], check=True)
    return path

def make_wiggle_gif(left, right, path, frames=20, duration=120):
    """Create a wiggle GIF that rapidly flips L/R — works WITHOUT any glasses."""
    imgs = []
    for i in range(frames):
        imgs.append(left if i % 2 == 0 else right)
    left.save(path, save_all=True, append_images=imgs[1:], duration=duration, loop=0)
    print(f"[VR Test] Saved wiggle.gif ({frames} frames, {duration}ms) — open it to see 3D!")

def make_cross_eye(left, right, path):
    """Side-by-side with a center divider for cross-eyed free viewing."""
    w, h = left.size
    out = Image.new("RGB", (w * 2 + 40, h + 60))
    out.paste(left, (0, 30))
    out.paste(right, (w + 40, 30))
    # Divider line
    px = out.load()
    for y in range(h + 60):
        px[w + 20, y] = (255, 255, 255)
    out.save(path)
    print(f"[VR Test] Saved cross-eye.png ({out.size[0]}x{out.size[1]})")

def make_anaglyph(left, right, path):
    """Red/Cyan anaglyph — view with cheap cardboard glasses."""
    w, h = left.size
    out = Image.new("RGB", (w, h))
    for y in range(h):
        for x in range(w):
            lr, lg, lb = left.getpixel((x, y))[:3]
            rr, rg, rb = right.getpixel((x, y))[:3]
            # Red channel from left eye, green+blue from right eye
            out.putpixel((x, y), (lr, rg, rb))
    out.save(path)
    print(f"[VR Test] Saved anaglyph.png ({w}x{h}) — view with RED/CYAN glasses")

def process_sbs(img_path):
    img = Image.open(img_path)
    w, h = img.size
    print(f"[VR Test] Screenshot size: {w}x{h}")

    mid = w // 2
    left = img.crop((0, 0, mid, h))
    right = img.crop((mid, 0, w, h))

    left.save(os.path.join(OUT_DIR, "left_eye.png"))
    right.save(os.path.join(OUT_DIR, "right_eye.png"))

    # Verify stereoscopic separation
    diff_count = 0
    sample_px = 50
    for y in range(0, h, max(1, h // sample_px)):
        for x in range(0, mid, max(1, mid // sample_px)):
            if left.getpixel((x, y)) != right.getpixel((x, y)):
                diff_count += 1
    print(f"[VR Test] Stereoscopic check: {diff_count}/{sample_px*sample_px} sampled pixels differ")
    if diff_count > 10:
        print("[VR Test] PASS: Left/right eye images are different")
    else:
        print("[VR Test] WARNING: Images look too similar")

    # Generate all no-headset formats
    make_wiggle_gif(left, right, os.path.join(OUT_DIR, "wiggle.gif"))
    make_cross_eye(left, right, os.path.join(OUT_DIR, "cross-eye.png"))
    make_anaglyph(left, right, os.path.join(OUT_DIR, "anaglyph.png"))

def show_no_headset_help():
    print("""
================================================================================
                    NO-HEADSET 3D VIEWING OPTIONS
================================================================================

1. WIGGLE GIF (easiest — zero equipment needed)
   File: vr-test-output/wiggle.gif
   How:  Just open it in any image viewer/browser. Rapidly switching
         L/R creates depth perception in your brain automatically.

2. CROSS-EYED FREE VIEWING (no glasses, but takes practice)
   File: vr-test-output/cross-eye.png
   How:  Hold your face about 30cm from the screen.
         Cross your eyes until the two inner images merge into one.
         The merged center image will appear in 3D.
         Tip: Focus on the divider line while relaxing your eyes outward.

3. ANAGLYPH (needs cheap red/cyan cardboard glasses)
   File: vr-test-output/anaglyph.png
   How:  Put on red-left / cyan-right 3D glasses.
         Works in any image viewer. Glasses cost ~$1 online.

4. BINO 3D PLAYER (on-screen stereo without glasses)
   Install: flatpak install flathub org.bino3d.bino
   How:    Record: wf-recorder -f /tmp/mc_sbs.mp4
           Play:   flatpak run org.bino3d.bino /tmp/mc_sbs.mp4
           Press V repeatedly until bottom-left says "ANAGLYPH"
           (This renders red/cyan on your monitor in real-time)

5. VR HEADSET (if you get one later)
   Use SteamVR, WiVRn, or Bigscreen. The F10 SBS split-screen
   is the standard format all VR video players accept.

================================================================================
""")

def main():
    print("=== MiniCraft VR SBS Test (No Headset Edition) ===")
    if not is_mc_running():
        start_mc()
    else:
        print("[VR Test] MiniCraft already running")

    print("[VR Test] Sending F9 (3D mode)...")
    send_key("F9")
    print("[VR Test] Sending F10 (VR SBS mode)...")
    send_key("F10")
    time.sleep(1)

    print("[VR Test] Taking screenshot...")
    ss = screenshot()
    process_sbs(ss)

    print(f"[VR Test] All output files in: {OUT_DIR}")
    show_no_headset_help()

if __name__ == "__main__":
    main()
