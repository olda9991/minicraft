#!/usr/bin/env python3
"""
MiniCraft VR SBS Test Script
Automates taking a screenshot of MiniCraft in VR mode and verifying
that left/right eye images are stereoscopically separated.
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

def process_sbs(img_path):
    img = Image.open(img_path)
    w, h = img.size
    print(f"[VR Test] Screenshot size: {w}x{h}")

    mid = w // 2
    left = img.crop((0, 0, mid, h))
    right = img.crop((mid, 0, w, h))

    left.save(os.path.join(OUT_DIR, "left_eye.png"))
    right.save(os.path.join(OUT_DIR, "right_eye.png"))
    print(f"[VR Test] Saved left_eye.png ({mid}x{h}) and right_eye.png ({w-mid}x{h})")

    # Verify stereoscopic separation
    diff_count = 0
    sample_px = 50
    for y in range(0, h, h // sample_px):
        for x in range(0, mid, mid // sample_px):
            if left.getpixel((x, y)) != right.getpixel((x, y)):
                diff_count += 1
    print(f"[VR Test] Stereoscopic check: {diff_count}/{sample_px*sample_px} sampled pixels differ")
    if diff_count > 10:
        print("[VR Test] PASS: Left/right eye images are different (stereoscopic separation working)")
    else:
        print("[VR Test] WARNING: Images look too similar — VR mode may not be active")

    # Create anaglyph (red/cyan) for glasses testing
    anaglyph = Image.new("RGB", (mid, h))
    for y in range(h):
        for x in range(mid):
            lr, lg, lb = left.getpixel((x, y))[:3]
            rr, rg, rb = right.getpixel((x, y))[:3]
            anaglyph.putpixel((x, y), (lr, rg, rb))
    anaglyph.save(os.path.join(OUT_DIR, "anaglyph.png"))
    print("[VR Test] Saved anaglyph.png (view with red/cyan 3D glasses)")

    return left, right

def show_vr_viewer_tip():
    print("""
[VR Test] How to view MiniCraft SBS in real-time:

    Option A - Bino 3D Player (installed):
        1. Launch MiniCraft, press F9 then F10
        2. Record: wf-recorder -f /tmp/mc_sbs.mp4
        3. Play: flatpak run org.bino3d.bino /tmp/mc_sbs.mp4
           In Bino, press V until it says "SBS" (side-by-side mode)

    Option B - Anaglyph (red/cyan glasses):
        Open vr-test-output/anaglyph.png in any image viewer

    Option C - VR headset (SteamVR / Bigscreen / WiVRn):
        1. Enable F10 SBS mode in MiniCraft
        2. Use SteamVR Desktop View or WiVRn to mirror the window
        3. Your headset should detect SBS automatically, or set it manually
""")

def main():
    print("=== MiniCraft VR SBS Test ===")
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

    print(f"[VR Test] Output files in: {OUT_DIR}")
    show_vr_viewer_tip()

if __name__ == "__main__":
    main()
