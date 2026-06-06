package com.example.skillfoliofinal

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "skillfolio.db"
        private const val DATABASE_VERSION = 2

        // ── Tabel Users ──────────────────────────────────────────────────────────
        private const val TABLE_USERS        = "users"
        private const val COL_U_ID           = "id"
        private const val COL_U_NAMA         = "nama"
        private const val COL_U_EMAIL        = "email"
        private const val COL_U_PHONE        = "phone"
        private const val COL_U_PRODI        = "prodi"
        private const val COL_U_UNIVERSITAS  = "universitas"
        private const val COL_U_PASSWORD     = "password"
        private const val COL_U_BIO          = "bio"
        private const val COL_U_LOKASI       = "lokasi"
        private const val COL_U_LINKEDIN     = "linkedin"
        private const val COL_U_GITHUB       = "github"
        private const val COL_U_FOTO_URI     = "foto_uri"

        // ── Tabel Pengalaman ─────────────────────────────────────────────────────
        private const val TABLE_PENGALAMAN    = "pengalaman"
        private const val COL_P_ID            = "id"
        private const val COL_P_JUDUL         = "judul"
        private const val COL_P_ORGANISASI    = "organisasi"
        private const val COL_P_POSISI        = "posisi"
        private const val COL_P_TAHUN_MULAI   = "tahun_mulai"
        private const val COL_P_TAHUN_SELESAI = "tahun_selesai"
        private const val COL_P_DESKRIPSI     = "deskripsi"
        private const val COL_P_USER_EMAIL    = "user_email"

        // ── Tabel Project ────────────────────────────────────────────────────────
        private const val TABLE_PROJECT      = "project"
        private const val COL_PR_ID          = "id"
        private const val COL_PR_NAMA        = "nama_project"
        private const val COL_PR_TEKNOLOGI   = "teknologi"
        private const val COL_PR_DESKRIPSI   = "deskripsi"
        private const val COL_PR_LINK        = "link"
        private const val COL_PR_TAHUN       = "tahun"
        private const val COL_PR_USER_EMAIL  = "user_email"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Tabel Users
        val createUsersTable = (
            "CREATE TABLE $TABLE_USERS (" +
            "$COL_U_ID          INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "$COL_U_NAMA        TEXT    NOT NULL, " +
            "$COL_U_EMAIL       TEXT    NOT NULL UNIQUE, " +
            "$COL_U_PHONE       TEXT, " +
            "$COL_U_PRODI       TEXT, " +
            "$COL_U_UNIVERSITAS TEXT, " +
            "$COL_U_PASSWORD    TEXT    NOT NULL, " +
            "$COL_U_BIO         TEXT    DEFAULT '', " +
            "$COL_U_LOKASI      TEXT    DEFAULT '', " +
            "$COL_U_LINKEDIN    TEXT    DEFAULT '', " +
            "$COL_U_GITHUB      TEXT    DEFAULT '', " +
            "$COL_U_FOTO_URI    TEXT    DEFAULT '')")
        db.execSQL(createUsersTable)

        // Tabel Pengalaman
        val createPengalamanTable = (
            "CREATE TABLE $TABLE_PENGALAMAN (" +
            "$COL_P_ID            INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "$COL_P_JUDUL         TEXT, " +
            "$COL_P_ORGANISASI    TEXT, " +
            "$COL_P_POSISI        TEXT, " +
            "$COL_P_TAHUN_MULAI   TEXT, " +
            "$COL_P_TAHUN_SELESAI TEXT, " +
            "$COL_P_DESKRIPSI     TEXT, " +
            "$COL_P_USER_EMAIL    TEXT, " +
            "FOREIGN KEY($COL_P_USER_EMAIL) REFERENCES $TABLE_USERS($COL_U_EMAIL))")
        db.execSQL(createPengalamanTable)

        // Tabel Project
        val createProjectTable = (
            "CREATE TABLE $TABLE_PROJECT (" +
            "$COL_PR_ID         INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "$COL_PR_NAMA       TEXT, " +
            "$COL_PR_TEKNOLOGI  TEXT, " +
            "$COL_PR_DESKRIPSI  TEXT, " +
            "$COL_PR_LINK       TEXT, " +
            "$COL_PR_TAHUN      TEXT, " +
            "$COL_PR_USER_EMAIL TEXT, " +
            "FOREIGN KEY($COL_PR_USER_EMAIL) REFERENCES $TABLE_USERS($COL_U_EMAIL))")
        db.execSQL(createProjectTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PENGALAMAN")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PROJECT")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // CRUD USERS
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Mendaftarkan user baru.
     * @return  row-id baru jika sukses, -1 jika email sudah ada / error.
     */
    fun registerUser(nama: String, email: String, phone: String,
                     prodi: String, universitas: String, password: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_U_NAMA, nama)
            put(COL_U_EMAIL, email.lowercase().trim())
            put(COL_U_PHONE, phone)
            put(COL_U_PRODI, prodi)
            put(COL_U_UNIVERSITAS, universitas)
            put(COL_U_PASSWORD, password)   // TODO: hash password di production
        }
        val result = db.insertWithOnConflict(
            TABLE_USERS, null, values, SQLiteDatabase.CONFLICT_IGNORE)
        db.close()
        return result
    }

    /**
     * Memverifikasi login.
     * @return  User jika email+password cocok, null jika tidak.
     */
    fun loginUser(email: String, password: String): User? {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_USERS WHERE LOWER($COL_U_EMAIL)=? AND $COL_U_PASSWORD=?",
            arrayOf(email.lowercase().trim(), password)
        )
        var user: User? = null
        if (cursor.moveToFirst()) {
            user = cursorToUser(cursor)
        }
        cursor.close()
        db.close()
        return user
    }

    /**
     * Mengambil data user berdasarkan email.
     */
    fun getUserByEmail(email: String): User? {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_USERS WHERE LOWER($COL_U_EMAIL)=?",
            arrayOf(email.lowercase().trim())
        )
        var user: User? = null
        if (cursor.moveToFirst()) {
            user = cursorToUser(cursor)
        }
        cursor.close()
        db.close()
        return user
    }

    /**
     * Memperbarui profil user.
     * @return  jumlah baris yang diperbarui (1 jika sukses).
     */
    fun updateUser(user: User): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_U_NAMA, user.nama)
            put(COL_U_PHONE, user.phone)
            put(COL_U_PRODI, user.prodi)
            put(COL_U_UNIVERSITAS, user.universitas)
            put(COL_U_PASSWORD, user.password)
            put(COL_U_BIO, user.bio)
            put(COL_U_LOKASI, user.lokasi)
            put(COL_U_LINKEDIN, user.linkedin)
            put(COL_U_GITHUB, user.github)
            put(COL_U_FOTO_URI, user.fotoUri)
        }
        val result = db.update(
            TABLE_USERS, values,
            "LOWER($COL_U_EMAIL)=?",
            arrayOf(user.email.lowercase().trim())
        )
        db.close()
        return result
    }

    /** Cek apakah email sudah terdaftar. */
    fun isEmailTaken(email: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT $COL_U_ID FROM $TABLE_USERS WHERE LOWER($COL_U_EMAIL)=?",
            arrayOf(email.lowercase().trim())
        )
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }

    private fun cursorToUser(cursor: android.database.Cursor): User {
        return User(
            id           = cursor.getInt(cursor.getColumnIndexOrThrow(COL_U_ID)),
            nama         = cursor.getString(cursor.getColumnIndexOrThrow(COL_U_NAMA)),
            email        = cursor.getString(cursor.getColumnIndexOrThrow(COL_U_EMAIL)),
            phone        = cursor.getString(cursor.getColumnIndexOrThrow(COL_U_PHONE)) ?: "",
            prodi        = cursor.getString(cursor.getColumnIndexOrThrow(COL_U_PRODI)) ?: "",
            universitas  = cursor.getString(cursor.getColumnIndexOrThrow(COL_U_UNIVERSITAS)) ?: "",
            password     = cursor.getString(cursor.getColumnIndexOrThrow(COL_U_PASSWORD)),
            bio          = cursor.getString(cursor.getColumnIndexOrThrow(COL_U_BIO)) ?: "",
            lokasi       = cursor.getString(cursor.getColumnIndexOrThrow(COL_U_LOKASI)) ?: "",
            linkedin     = cursor.getString(cursor.getColumnIndexOrThrow(COL_U_LINKEDIN)) ?: "",
            github       = cursor.getString(cursor.getColumnIndexOrThrow(COL_U_GITHUB)) ?: "",
            fotoUri      = cursor.getString(cursor.getColumnIndexOrThrow(COL_U_FOTO_URI)) ?: ""
        )
    }

    // CRUD Pengalaman
    fun insertPengalaman(p: Pengalaman): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_P_JUDUL, p.judul)
            put(COL_P_ORGANISASI, p.organisasi)
            put(COL_P_POSISI, p.posisi)
            put(COL_P_TAHUN_MULAI, p.tahunMulai)
            put(COL_P_TAHUN_SELESAI, p.tahunSelesai)
            put(COL_P_DESKRIPSI, p.deskripsi)
            put(COL_P_USER_EMAIL, p.userEmail)
        }
        val result = db.insert(TABLE_PENGALAMAN, null, values)
        db.close()
        return result
    }

    fun getAllPengalaman(userEmail: String): List<Pengalaman> {
        val list = ArrayList<Pengalaman>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_PENGALAMAN WHERE $COL_P_USER_EMAIL = ?", arrayOf(userEmail))

        if (cursor.moveToFirst()) {
            do {
                val p = Pengalaman(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_P_ID)),
                    judul = cursor.getString(cursor.getColumnIndexOrThrow(COL_P_JUDUL)),
                    organisasi = cursor.getString(cursor.getColumnIndexOrThrow(COL_P_ORGANISASI)),
                    posisi = cursor.getString(cursor.getColumnIndexOrThrow(COL_P_POSISI)),
                    tahunMulai = cursor.getString(cursor.getColumnIndexOrThrow(COL_P_TAHUN_MULAI)),
                    tahunSelesai = cursor.getString(cursor.getColumnIndexOrThrow(COL_P_TAHUN_SELESAI)),
                    deskripsi = cursor.getString(cursor.getColumnIndexOrThrow(COL_P_DESKRIPSI)),
                    userEmail = cursor.getString(cursor.getColumnIndexOrThrow(COL_P_USER_EMAIL))
                )
                list.add(p)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return list
    }

    fun updatePengalaman(p: Pengalaman): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_P_JUDUL, p.judul)
            put(COL_P_ORGANISASI, p.organisasi)
            put(COL_P_POSISI, p.posisi)
            put(COL_P_TAHUN_MULAI, p.tahunMulai)
            put(COL_P_TAHUN_SELESAI, p.tahunSelesai)
            put(COL_P_DESKRIPSI, p.deskripsi)
            put(COL_P_USER_EMAIL, p.userEmail)
        }
        val result = db.update(TABLE_PENGALAMAN, values, "$COL_P_ID = ?", arrayOf(p.id.toString()))
        db.close()
        return result
    }

    fun deletePengalaman(id: Int): Int {
        val db = this.writableDatabase
        val result = db.delete(TABLE_PENGALAMAN, "$COL_P_ID = ?", arrayOf(id.toString()))
        db.close()
        return result
    }

    fun getPengalamanById(id: Int): Pengalaman? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_PENGALAMAN WHERE $COL_P_ID = ?", arrayOf(id.toString()))
        var p: Pengalaman? = null
        if (cursor.moveToFirst()) {
            p = Pengalaman(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_P_ID)),
                judul = cursor.getString(cursor.getColumnIndexOrThrow(COL_P_JUDUL)),
                organisasi = cursor.getString(cursor.getColumnIndexOrThrow(COL_P_ORGANISASI)),
                posisi = cursor.getString(cursor.getColumnIndexOrThrow(COL_P_POSISI)),
                tahunMulai = cursor.getString(cursor.getColumnIndexOrThrow(COL_P_TAHUN_MULAI)),
                tahunSelesai = cursor.getString(cursor.getColumnIndexOrThrow(COL_P_TAHUN_SELESAI)),
                deskripsi = cursor.getString(cursor.getColumnIndexOrThrow(COL_P_DESKRIPSI)),
                userEmail = cursor.getString(cursor.getColumnIndexOrThrow(COL_P_USER_EMAIL))
            )
        }
        cursor.close()
        db.close()
        return p
    }

    // CRUD Project
    fun insertProject(p: Project): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_PR_NAMA, p.namaProject)
            put(COL_PR_TEKNOLOGI, p.teknologi)
            put(COL_PR_DESKRIPSI, p.deskripsi)
            put(COL_PR_LINK, p.link)
            put(COL_PR_TAHUN, p.tahun)
            put(COL_PR_USER_EMAIL, p.userEmail)
        }
        val result = db.insert(TABLE_PROJECT, null, values)
        db.close()
        return result
    }

    fun getAllProject(userEmail: String): List<Project> {
        val list = ArrayList<Project>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_PROJECT WHERE $COL_PR_USER_EMAIL = ?", arrayOf(userEmail))

        if (cursor.moveToFirst()) {
            do {
                val p = Project(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PR_ID)),
                    namaProject = cursor.getString(cursor.getColumnIndexOrThrow(COL_PR_NAMA)),
                    teknologi = cursor.getString(cursor.getColumnIndexOrThrow(COL_PR_TEKNOLOGI)),
                    deskripsi = cursor.getString(cursor.getColumnIndexOrThrow(COL_PR_DESKRIPSI)),
                    link = cursor.getString(cursor.getColumnIndexOrThrow(COL_PR_LINK)),
                    tahun = cursor.getString(cursor.getColumnIndexOrThrow(COL_PR_TAHUN)),
                    userEmail = cursor.getString(cursor.getColumnIndexOrThrow(COL_PR_USER_EMAIL))
                )
                list.add(p)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return list
    }

    fun updateProject(p: Project): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_PR_NAMA, p.namaProject)
            put(COL_PR_TEKNOLOGI, p.teknologi)
            put(COL_PR_DESKRIPSI, p.deskripsi)
            put(COL_PR_LINK, p.link)
            put(COL_PR_TAHUN, p.tahun)
            put(COL_PR_USER_EMAIL, p.userEmail)
        }
        val result = db.update(TABLE_PROJECT, values, "$COL_PR_ID = ?", arrayOf(p.id.toString()))
        db.close()
        return result
    }

    fun deleteProject(id: Int): Int {
        val db = this.writableDatabase
        val result = db.delete(TABLE_PROJECT, "$COL_PR_ID = ?", arrayOf(id.toString()))
        db.close()
        return result
    }

    fun getProjectById(id: Int): Project? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_PROJECT WHERE $COL_PR_ID = ?", arrayOf(id.toString()))
        var p: Project? = null
        if (cursor.moveToFirst()) {
            p = Project(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PR_ID)),
                namaProject = cursor.getString(cursor.getColumnIndexOrThrow(COL_PR_NAMA)),
                teknologi = cursor.getString(cursor.getColumnIndexOrThrow(COL_PR_TEKNOLOGI)),
                deskripsi = cursor.getString(cursor.getColumnIndexOrThrow(COL_PR_DESKRIPSI)),
                link = cursor.getString(cursor.getColumnIndexOrThrow(COL_PR_LINK)),
                tahun = cursor.getString(cursor.getColumnIndexOrThrow(COL_PR_TAHUN)),
                userEmail = cursor.getString(cursor.getColumnIndexOrThrow(COL_PR_USER_EMAIL))
            )
        }
        cursor.close()
        db.close()
        return p
    }
}
