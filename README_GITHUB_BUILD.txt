WHAT'S FOR TODAY? — BUILD THE APK WITHOUT ANDROID STUDIO

This project is configured to build on GitHub's cloud computers. You do not need
Android Studio or Android build tools on your own device.

PHONE-ONLY STEPS

1. Sign in to https://github.com (create a free account if needed).
2. Open https://github.com/new
3. Name the repository: WhatsForToday
4. Choose Public (simplest for free GitHub Actions), leave other settings as they
   are, and tap Create repository.
5. In the new repository, choose Add file -> Upload files.
6. Upload the CONTENTS of this project folder, including the .github folder.
   The .github/workflows/build-apk.yml file is essential. If your file picker
   hides dot-folders, create the workflow manually:
   - Add file -> Create new file
   - Filename: .github/workflows/build-apk.yml
   - Paste the contents from the supplied build-apk.yml file.
7. Commit the uploaded files.
8. Open the repository's Actions tab. If GitHub asks, enable workflows.
9. Select "Build What's for Today APK" on the left, tap Run workflow, then
   tap the green Run workflow button.
10. Wait for the run to finish. Open the completed run and find Artifacts near
    the bottom. Download WhatsForToday-APK.
11. Extract the downloaded ZIP on your phone, tap app-debug.apk, and allow
    installation from your browser/files app if Android asks.

NOTES
- The APK is a debug-signed installable APK for personal use, not a Play Store
  release.
- The app stores your meals locally on your phone using SharedPreferences.
- Keep the GitHub repository public if you want to use GitHub's free Actions
  minutes without private-repository plan limits.
- Do not upload passwords, signing keys, or personal data to the repository.
