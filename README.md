# WASSCE Prep Ghana 🇬🇭

A comprehensive, offline-first study companion designed specifically for Ghanaian Senior High School (SHS) candidates to master the WAEC/WASSCE syllabus, drill past questions (Pasco) under timed exam conditions, collaborate with classmates in live quiz battles, import PDF textbooks for Gemini AI study guides, transcribe classroom lectures, and sync progress with Firebase.

---

## 📱 Download & Install the APK

You can download and install the app directly on any Android device:

### Option 1: Direct APK Download from this Repository
1. Navigate to the [`apk/`](apk/) directory in this repository.
2. Click on **[`WASSCE_Prep.apk`](apk/WASSCE_Prep.apk)**.
3. Click the **Download raw file** button (or download directly to your Android device).
4. On your Android phone, open the downloaded file.
5. If prompted by Android, enable **"Allow installation from this source"**.
6. Tap **Install** and open **WASSCE Prep**!

### Option 2: Automated GitHub Releases / Actions
- The repository includes a GitHub Actions workflow (`.github/workflows/build-apk.yml`) that automatically compiles the latest APK on every push. You can also download the latest compiled artifact from the **Actions** tab on GitHub.

---

## ✨ Key Features

### 1. 📚 Official WASSCE Syllabus Progress Tracker
- **All Major SHS Programmes**: Core Subjects (Core Mathematics, English Language, Integrated Science, Social Studies) plus Electives for **General Science** (Elective Maths, Physics, Chemistry, Biology), **General Arts** (Government, Economics), and **Business** (Financial Accounting, Business Management).
- **Mastery Levels**: Track each unit with status chips (`Not Started`, `Revising`, `Mastered`) and 5-star confidence ratings.
- **High-Yield Indicators**: Highlights high-frequency WAEC topics that routinely appear on Paper 1 and Paper 2.
- **Readiness Meter**: Real-time gauge calculating overall syllabus mastery towards Aggregate 6 to 8.

### 2. 📝 Offline Past Questions (Pasco) Exam Simulator
- **Timed Mock Exam Mode**: Simulates authentic WAEC Paper 1 conditions with a 25-minute countdown, timer alert, question sheet navigation drawer, and bookmarks.
- **Topical Practice Drill**: Untimed study mode focusing on specific syllabus topics.
- **Step-by-Step Solutions & WAEC Examiner Tips**: Immediate answer explanations, mathematical derivations, and Chief Examiner warnings.
- **Scorecards & Grades**: Automatic grading with estimated WASSCE letter grades (`A1 (Excellent)` to `F9`).

### 3. 🤖 Gemini AI Textbook Study Guides (`gemini-3.8-flash`)
- **Import Custom PDF Textbooks**: Use the system document picker to import any PDF textbook, past handout, or class notes.
- **Pre-loaded Ghanaian Textbooks**: Includes chapters from *Aki-Ola Core Mathematics*, *Ababio New School Chemistry*, *Integrated Science for SHS*, and *Principles of Economics*.
- **Structured AI Guides**: Real-time streaming output generating high-yield chapter summaries, core formulas to memorize, WAEC Chief Examiner pitfalls, and diagnostic practice quizzes.
- **100% Offline Room Caching**: Save generated guides directly to the local database to read anytime without internet connectivity.

### 4. 🔍 Google Search Grounding for WAEC Facts
- Powered by `gemini-3.8-flash` with Google Search Grounding (`Tool.googleSearch()`).
- Ask questions about current WAEC timetables, chief examiner reports, grading boundaries, and syllabus updates with verified web sources.

### 5. 🎙️ Classroom Audio Transcriber (`gemini-3.5-transcribe`)
- Record classroom lessons or prep explanations using your device's microphone.
- Transcribes audio into verbatim English text using Google's `gemini-3.5-transcribe` model.
- Includes sample teacher recordings for testing in environments without physical microphones.
- Automatically syncs transcripts to your cloud notes.

### 6. ⚔️ Study Squad & Competitive Learning
- **1v1 Live Quiz Battle Arena**: Head-to-head 5-question speed duels with a 15-second timer per question, real-time score comparison, and XP victory podiums.
- **Virtual Prep Rooms**: Synchronized Pomodoro focus sessions with classmate presence and cheer reactions (👏 High Five, 🔥 On Fire, 💡 Eureka, 📚 Keep Pushing).
- **Ghana SHS Leaderboard**: Weekly rankings highlighting top candidates and schools (PRESEC Legon, Wesley Girls, Prempeh College, Achimota, Mfantsipim, Holy Child, Opoku Ware).

### 7. ☁️ Firebase Authentication & Cloud Sync
- **Google Sign-In**: Integrated with Jetpack Credential Manager for secure, seamless sign-in.
- **Firestore Enterprise Persistence**: Backs up syllabus progress, exam attempts, research queries, and audio notes to the cloud.

---

## 🛠️ Building from Source

```bash
# Clone the repository
git clone https://github.com/your-username/wassce-prep-ghana.git
cd wassce-prep-ghana

# Build the debug APK
gradle assembleDebug

# Output APK location:
# app/build/outputs/apk/debug/app-debug.apk
```

---

## 📄 License
This application is created for Ghanaian Senior High School students preparing for the West African Senior School Certificate Examination (WASSCE).
