package com.syads.myganesa.assets;

public class User {
    private String username, role;
    private String nama, tgl_lahir, kelas, agama, id_kelas;
    private String kd_guru, nama_matpel;
    private int id, saldo;

    //Siswa
    public User(int id, String username, String role,
                String nama, String kelas, String tgl_lahir, String agama,
                String id_kelas, int saldo) {
        this.username = username;
        this.role = role;
        this.nama = nama;
        this.tgl_lahir = tgl_lahir;
        this.kelas = kelas;
        this.agama = agama;
        this.id = id;
        this.id_kelas = id_kelas;
        this.saldo = saldo;
    }
    //Guru
    public User(int id, String username, String role,
                String nama, String kd_guru, String nama_matpel) {
        this.username = username;
        this.role = role;
        this.nama = nama;
        this.kd_guru = kd_guru;
        this.id = id;
        this.nama_matpel = nama_matpel;
    }
    //Bendahara
    public User(int id, String username, String role) {
        this.username = username;
        this.role = role;
        this.id = id;
    }

    public String getNama_matpel() {
        return nama_matpel;
    }

    public String getKd_guru() {
        return kd_guru;
    }

    public String getId_kelas() {
        return id_kelas;
    }

    public String getUsername() {
        return username;
    }
    public String getRole() {
        return role;
    }

    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getTgl_lahir() {
        return tgl_lahir;
    }

    public String getKelas() {
        return kelas;
    }

    public String getAgama() {
        return agama;
    }

    public int getSaldo() {
        return saldo;
    }
}
