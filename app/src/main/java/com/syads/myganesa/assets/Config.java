package com.syads.myganesa.assets;

public class Config {
    //Link PHP
    public static final String URL_ROOT = "http://192.168.8.102/sekerip_apip/android";
    public static final String URL_ACTION = URL_ROOT + "/api.php?action=";
    public static final String URL_LOGIN = URL_ACTION + "login";
    public static final String URL_ABSEN = URL_ACTION + "absen";
    public static final String URL_BAYAR = URL_ACTION + "bayar";
    public static final String URL_TOP_UP = URL_ACTION + "top_up";
    public static final String URL_REFRESH = URL_ACTION + "refresh_student";
    public static final String URL_GET_ALL = URL_ROOT + "/tampil_jadwal.php";
    public static final String URL_GET_PAID = URL_ROOT + "/tampil_pembayaran.php";
    public static final String URL_GET_PAID_TREASURER = URL_ROOT + "/tampil_pembayaran_bendahara.php";
    public static final String URL_GET_TODAYS_CLASS = URL_ROOT + "/tampil_kelas_ajar.php";
    public static final String URL_GET_CLASS = URL_ROOT + "/tampil_kelas.php";

    //JSON Tags
    public static final String TAG_JSON_ARRAY="result";
    public static final String TAG_JAMPEL = "jampel";
    public static final String TAG_KD_GURU = "kd_guru";
    public static final String TAG_NAMA = "nama";
    public static final String TAG_NAMA_MATPEL = "nama_matpel";

    public static final String TAG_BULAN = "bulan";
    public static final String TAG_TANGGAL_BAYAR = "tanggal_bayar";
    public static final String TAG_NOMINAL = "nominal";
    public static final String TAG_NIS = "nis";

    public static final String TAG_KELAS = "nama_kelas";
    public static final String CLASS_ID = "id_kelas";


}
