/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelDatabase;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import java.io.Serializable;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
/**
 *
 * @author ade
 */
@Entity
@Table(name = "retur_penjualan")
public class retur_penjualan {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false, length = 20)
    private Integer id;
    
    @Column(name = "no_returpenjualan", unique = true, nullable = false, length = 20)
    private String no_returpenjualan;
    
    @Column(name = "tanggal")
    private Timestamp tanggal;
    
    @OneToOne
    @JoinColumn(name="no_faktur")
    private penjualan no_faktur;
    
    @OneToOne
    @JoinColumn(name="id_pegawai")
    private pegawai id_pegawai;
    
    @OneToOne
    @JoinColumn(name="id_pelanggan")
    private pelanggan kode_pelanggan;
    
    
    @OneToOne
    @JoinColumn(name="id_barang")
    private barang kode_barang;
    
    @Column(name = "nama_barang")
    private String nama_barang;
    
    @Column(name = "jumlah")
    private Long jumlah;

    public pegawai getId_pegawai() {
        return id_pegawai;
    }

    public void setId_pegawai(pegawai id_pegawai) {
        this.id_pegawai = id_pegawai;
    }

    public barang getKode_barang() {
        return kode_barang;
    }

    public void setKode_barang(barang kode_barang) {
        this.kode_barang = kode_barang;
    }


    public Timestamp getTanggal() {
        return tanggal;
    }

    public void setTanggal(Timestamp tanggal) {
        this.tanggal = tanggal;
    }

    public Long getJumlah() {
        return jumlah;
    }

    public void setJumlah(Long jumlah) {
        this.jumlah = jumlah;
    }

    public pelanggan getKode_pelanggan() {
        return kode_pelanggan;
    }

    public void setKode_pelanggan(pelanggan kode_pelanggan) {
        this.kode_pelanggan = kode_pelanggan;
    }

    public String getNama_barang() {
        return nama_barang;
    }

    public void setNama_barang(String nama_barang) {
        this.nama_barang = nama_barang;
    }

    public penjualan getNo_faktur() {
        return no_faktur;
    }

    public void setNo_faktur(penjualan no_faktur) {
        this.no_faktur = no_faktur;
    }

    public String getNo_returpenjualan() {
        return no_returpenjualan;
    }

    public void setNo_returpenjualan(String no_returpenjualan) {
        this.no_returpenjualan = no_returpenjualan;
    }
}
