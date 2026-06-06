<div align="center">

<img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" />
<img src="https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" />
<img src="https://img.shields.io/badge/SQLite-003B57?style=for-the-badge&logo=sqlite&logoColor=white" />
<img src="https://img.shields.io/badge/Android_Studio-3DDC84?style=for-the-badge&logo=android-studio&logoColor=white" />

<br/><br/>

# 📱 SkillFolio
### *Your Digital Career Portfolio — Verified, Organized, Professional*

<br/>

> **SkillFolio** adalah aplikasi Android portofolio karir digital yang membantu mahasiswa dan pencari kerja mengorganisasi riwayat pengalaman, proyek, dan keahlian mereka secara terstruktur — lengkap dengan sistem verifikasi dan pembuatan CV digital.

<br/>

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
![Platform](https://img.shields.io/badge/Platform-Android-green)
![Min SDK](https://img.shields.io/badge/Min%20SDK-24-blue)
![Target SDK](https://img.shields.io/badge/Target%20SDK-35-blue)

</div>

---

## 🌟 Tentang Aplikasi

**SkillFolio** hadir sebagai solusi nyata untuk masalah yang dihadapi mahasiswa dan pencari kerja:

> *"Bagaimana cara menyajikan pengalaman dan pencapaian saya secara profesional dan terverifikasi?"*

Dengan SkillFolio, pengguna dapat membangun portofolio digital yang:
- 📝 **Terstruktur** — Riwayat pengalaman dan proyek tersaji rapi
- ✅ **Terverifikasi** — Pengalaman dapat divalidasi oleh dosen atau atasan
- 📊 **Terukur** — Progress kelengkapan profil terpantau secara visual
- 🎨 **Profesional** — UI/UX modern yang siap ditampilkan ke rekruiter

### 🌍 Relevansi SDGs
| SDG | Keterkaitan |
|-----|-------------|
| 🎓 **SDG 4** — Quality Education | Mendukung validasi kompetensi informal dan pembelajaran sepanjang hayat |
| 💼 **SDG 8** — Decent Work & Economic Growth | Membantu pencari kerja tampil profesional dan meningkatkan peluang kerja layak |

---

## ✨ Fitur Utama

<table>
<tr>
<td width="50%">

### 🔐 Autentikasi & Sesi
- Splash Screen animasi modern
- Registrasi akun baru dengan validasi lengkap
- Login aman dengan verifikasi database
- Session management via **SharedPreferences**
- Tidak perlu login ulang saat aplikasi dibuka kembali

</td>
<td width="50%">

### 👤 Manajemen Profil
- Upload & sinkronisasi foto profil (lingkaran) ke semua halaman
- Edit data diri, bio, lokasi
- Integrasi tautan LinkedIn & GitHub
- Progress kelengkapan profil (0–100%) real-time

</td>
</tr>
<tr>
<td width="50%">

### 📋 Portfolio Pengalaman
- Tambah, edit, hapus riwayat pengalaman kerja/organisasi
- Pilih keahlian/skill relevan untuk setiap pengalaman
- Checkbox "Masih bekerja" otomatis mengisi "Present"
- Filter & pencarian riwayat pengalaman
- Badge jumlah total pengalaman

</td>
<td width="50%">

### 🚀 Portfolio Proyek
- Tambah, edit, hapus portofolio proyek
- Input teknologi, link repositori/demo, dan tahun
- Tampilan kartu proyek yang informatif
- Filter & pencarian proyek

</td>
</tr>
<tr>
<td width="50%">

### ✅ Sistem Verifikasi
- Ajukan verifikasi pengalaman/proyek ke verifikator eksternal
- Form input data verifikator (nama, jabatan, email, hubungan)
- Halaman konfirmasi sukses verifikasi
- Status badge "Terverifikasi" / "Proses" pada ringkasan pengalaman

</td>
<td width="50%">

### 📄 CV Generator & Navigasi
- Generate tampilan CV digital dari data portofolio
- Navigasi bottom tab yang smooth antar halaman utama
- Transisi layar modern: **Fade + Scale** (tanpa slide PPT)
- Responsive layout untuk berbagai ukuran layar

</td>
</tr>
</table>

---

## 📱 Halaman Aplikasi

```
SkillFolio App
│
├── 🌟 SplashActivity          — Layar pembuka beranimasi
├── 📖 OnboardingActivity      — Pengenalan fitur utama
│
├── 🔐 Auth
│   ├── WelcomeActivity        — Pilih Login / Daftar
│   ├── LoginActivity          — Masuk ke akun
│   └── RegisterActivity       — Buat akun baru
│
├── 🏠 Dashboard (Bottom Nav)
│   ├── MainActivity           — Home: ringkasan profil & statistik
│   ├── ListPengalamanActivity — Daftar riwayat pengalaman
│   ├── ListProjectActivity    — Daftar portofolio proyek
│   └── ProfileActivity        — Profil pengguna lengkap
│
├── 📝 Form & Detail
│   ├── TambahPengalamanActivity  — Tambah/Edit pengalaman
│   ├── TambahProjectActivity     — Tambah/Edit proyek
│   ├── TambahKeahlianActivity    — Pilih keahlian/skill
│   ├── DetailPengalamanActivity  — Detail pengalaman
│   ├── DetailProjectActivity     — Detail proyek
│   └── EditProfilActivity        — Edit profil pengguna
│
├── ✅ Verifikasi
│   ├── VerifikasiActivity        — Form ajuan verifikasi
│   ├── VerifikasiSuksesActivity  — Konfirmasi sukses
│   └── VerifikasiDetailActivity  — Detail status verifikasi
│
└── 📊 Lainnya
    ├── KelengkapanProfilActivity — Progress profil (%)
    ├── TotalPengalamanActivity   — Statistik total pengalaman
    ├── CvGeneratorActivity       — Generator CV digital
    └── PengaturanActivity        — Pengaturan akun & logout
```

---

## 🛠️ Teknologi yang Digunakan

| Kategori | Teknologi |
|----------|-----------|
| **Bahasa** | Kotlin |
| **Platform** | Android Native (Min SDK 24) |
| **Database** | SQLite (via `SQLiteOpenHelper`) |
| **Sesi Pengguna** | SharedPreferences |
| **UI Components** | Material Design 3, RecyclerView, BottomNavigationView |
| **Gambar Profil** | ShapeableImageView (lingkaran), URI lokal |
| **Animasi** | Custom XML Animator (Fade + Scale, tanpa slide) |
| **IDE** | Android Studio |
| **Version Control** | Git & GitHub |

---

## 🗄️ Struktur Database (SQLite)

### Tabel `users`
| Kolom | Tipe | Keterangan |
|-------|------|------------|
| `id` | INTEGER PK | Auto-increment |
| `nama` | TEXT | Nama lengkap pengguna |
| `email` | TEXT UNIQUE | Email (kunci unik) |
| `phone` | TEXT | Nomor telepon |
| `prodi` | TEXT | Program studi |
| `universitas` | TEXT | Nama universitas |
| `password` | TEXT | Password akun |
| `bio` | TEXT | Deskripsi singkat |
| `lokasi` | TEXT | Kota/lokasi |
| `linkedin` | TEXT | URL profil LinkedIn |
| `github` | TEXT | URL profil GitHub |
| `foto_uri` | TEXT | URI foto profil lokal |

### Tabel `pengalaman`
| Kolom | Tipe | Keterangan |
|-------|------|------------|
| `id` | INTEGER PK | Auto-increment |
| `judul` | TEXT | Judul/nama posisi |
| `organisasi` | TEXT | Nama organisasi/perusahaan |
| `posisi` | TEXT | Jabatan/posisi |
| `tahun_mulai` | TEXT | Periode mulai |
| `tahun_selesai` | TEXT | Periode selesai / "Present" |
| `deskripsi` | TEXT | Deskripsi pekerjaan/kegiatan |
| `user_email` | TEXT (FK) | Relasi ke tabel users |

### Tabel `project`
| Kolom | Tipe | Keterangan |
|-------|------|------------|
| `id` | INTEGER PK | Auto-increment |
| `nama_project` | TEXT | Nama proyek |
| `teknologi` | TEXT | Stack teknologi yang digunakan |
| `deskripsi` | TEXT | Deskripsi proyek |
| `link` | TEXT | Link repositori/demo |
| `tahun` | TEXT | Tahun pembuatan |
| `user_email` | TEXT (FK) | Relasi ke tabel users |

---

## ⚡ CRUD Operations

| Operasi | Pengalaman | Proyek | Pengguna |
|---------|:----------:|:------:|:--------:|
| **Create** | `insertPengalaman()` | `insertProject()` | `registerUser()` |
| **Read** | `getAllPengalaman()` `getPengalamanById()` | `getAllProject()` `getProjectById()` | `getUserByEmail()` `loginUser()` |
| **Update** | `updatePengalaman()` | `updateProject()` | `updateUser()` |
| **Delete** | `deletePengalaman()` | `deleteProject()` | — |

---

## 🚀 Cara Menjalankan Proyek

### Prasyarat
- Android Studio **Hedgehog** atau lebih baru
- JDK 17+
- Android SDK (Min API 24, Target API 35)
- Perangkat/Emulator Android

### Langkah Instalasi

```bash
# 1. Clone repository ini
git clone https://github.com/kakaadsp/SkillFolio-Final.git

# 2. Buka dengan Android Studio
# File → Open → Pilih folder SkillFolio-Final

# 3. Tunggu Gradle sync selesai

# 4. Jalankan aplikasi
# Klik tombol ▶ Run atau tekan Shift+F10
```

---

## 📂 Struktur Proyek

```
SkillFolio-Final/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/skillfoliofinal/
│   │   │   ├── 📦 Model
│   │   │   │   ├── User.kt
│   │   │   │   ├── Pengalaman.kt
│   │   │   │   └── Project.kt
│   │   │   ├── 🗄️ Database
│   │   │   │   └── DatabaseHelper.kt
│   │   │   ├── 🛠️ Utility
│   │   │   │   └── AppUtils.kt
│   │   │   ├── 📋 Adapter
│   │   │   │   ├── PengalamanAdapter.kt
│   │   │   │   └── ProjectAdapter.kt
│   │   │   └── 📱 Activity (29 halaman)
│   │   │       └── ...
│   │   └── res/
│   │       ├── layout/       — File XML tampilan (29 layout)
│   │       ├── drawable/     — Ikon, background, shape
│   │       ├── anim/         — Animasi fade+scale custom
│   │       └── values/       — Colors, strings, themes
│   └── build.gradle.kts
├── README.md
└── ...
```

---

## 👥 Tim Pengembang

> **Kelompok — Pemrograman Mobile / [Nama Mata Kuliah]**

<table>
<tr>
<td align="center" width="25%">
<br/>
<b>👑 Kaka Dimas Soehendra Putra</b><br/>
<sub>NPM: 171</sub><br/><br/>
<img src="https://img.shields.io/badge/Role-Lead%20%26%20Integrator-FFD600?style=flat-square&labelColor=333"/>
<br/><br/>

**Tugas Teknis:**
- Merancang & mengimplementasikan Database SQLite (`DatabaseHelper.kt`)
- Mengatur Session/SharedPreferences & logika Login
- Integrasi seluruh modul menjadi satu aplikasi utuh

**Tugas Laporan:**
- Menulis BAB I (Pendahuluan) & BAB II (Landasan Teori)
- Merakit seluruh BAB menjadi laporan final
- Mengatur Git/GitHub repository & version control
</td>
<td align="center" width="25%">
<br/>
<b>🎨 Cutrin Joy M.T Sihombing</b><br/>
<sub>NPM: 170</sub><br/><br/>
<img src="https://img.shields.io/badge/Role-UI%2FUX%20%26%20Auth%20Frontend-4A90D9?style=flat-square&labelColor=333"/>
<br/><br/>

**Tugas Teknis:**
- Merancang desain UI/UX di **Figma**
- Mengkoding layout XML untuk **Splash Screen**, **Login**, dan **Register**
- Menerapkan desain yang konsisten pada halaman autentikasi

**Tugas Laporan:**
- Membuat mockup UI untuk dimasukkan ke laporan
- Menulis dokumentasi UI/UX
</td>
<td align="center" width="25%">
<br/>
<b>🏗️ Gratia Novelin Tamba</b><br/>
<sub>NPM: 178</sub><br/><br/>
<img src="https://img.shields.io/badge/Role-Frontend%20Main%20%26%20Navigation-27AE60?style=flat-square&labelColor=333"/>
<br/><br/>

**Tugas Teknis:**
- Mengkoding XML untuk **Dashboard** (Home, List Data, RecyclerView)
- Mengkoding halaman **Form Tambah/Edit** Data
- Mengatur **Navigasi** antar layar (Intent & Bottom Navigation)

**Tugas Laporan:**
- Menyiapkan screenshot aplikasi yang berjalan
- Mendokumentasikan tampilan untuk BAB III
</td>
<td align="center" width="25%">
<br/>
<b>🔗 Lutfia Nur Sabrina</b><br/>
<sub>NPM: 175</sub><br/><br/>
<img src="https://img.shields.io/badge/Role-Backend%20Logic%20%26%20QA%20Tester-E74C3C?style=flat-square&labelColor=333"/>
<br/><br/>

**Tugas Teknis:**
- Menghubungkan tampilan (XML) dengan **Database** (CRUD)
- Memastikan semua fungsi **Create, Read, Update, Delete** berjalan lancar
- Integrasi komponen UI dengan logika backend

**Tugas Laporan:**
- Melakukan **Black Box Testing**
- Membuat tabel pengujian
- Menulis BAB IV (Pengujian & Hasil)
</td>
</tr>
</table>

---

## ✅ Pemenuhan Ketentuan Proyek

| # | Ketentuan | Status | Implementasi |
|---|-----------|:------:|--------------|
| 1 | Splash Screen | ✅ | `SplashActivity.kt` dengan animasi fade masuk |
| 2 | Login / Register | ✅ | `LoginActivity.kt` + `RegisterActivity.kt` |
| 3 | Session User | ✅ | `SharedPreferences` (`skillfolio_prefs`) |
| 4 | CRUD Data | ✅ | Pengalaman & Proyek — Create, Read, Update, Delete penuh |
| 5 | RecyclerView / List Data | ✅ | `ListPengalamanActivity`, `ListProjectActivity`, `TotalPengalamanActivity` |
| 6 | Navigation | ✅ | `BottomNavigationView` + Intent antar layar |
| 7 | Validasi Input | ✅ | Validasi kosong di semua form + Toast feedback |
| 8 | Database | ✅ | SQLite via `DatabaseHelper.kt` (3 tabel relasional) |
| 9 | Minimal 4 Halaman Utama | ✅ | **29 halaman** — Home, Pengalaman, Proyek, Profil, dll |
| 10 | UI/UX Konsisten | ✅ | Skema warna kuning/gelap, avatar lingkaran, transisi smooth seragam |
| 11 | Studi Kasus Nyata | ✅ | Portofolio karir digital & CV generator terverifikasi |
| 12 | Alur Penggunaan Jelas | ✅ | Daftar → Login → Isi Profil → Tambah Data → Verifikasi → Ekspor CV |
| 13 | Relevan dengan Tema SDGs | ✅ | SDG 4 (Pendidikan Berkualitas) & SDG 8 (Pekerjaan Layak) |

---

## 📋 Changelog

| Versi | Keterangan |
|-------|------------|
| `v1.0.0` | Rilis awal: Auth, CRUD Pengalaman & Proyek, Profil, Navigasi |
| `v1.1.0` | Sinkronisasi foto profil lingkaran di semua halaman |
| `v1.2.0` | Transisi smooth fade+scale di seluruh layar (ganti slide) |
| `v1.3.0` | Perbaikan ikon hapus, integrasi AppUtils terpusat |
| `v1.4.0` | Smooth transition pada halaman Verifikasi |

---

<div align="center">

### 🏫 Informasi Akademis

**Universitas / Institusi Pendidikan**
Mata Kuliah Pemrograman Mobile — 2025/2026

---

<sub>Dibuat dengan ❤️ oleh Tim SkillFolio — Kelompok Pemrograman Mobile 2025/2026</sub>
<br/>
<sub>📱 Android • Kotlin • SQLite • Material Design 3</sub>

</div>
