/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelDatabase;

import java.io.Serializable;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
/**
 *
 * @author ade
 */
@Entity
@Table(name = "klaim_garansi")
public class klaim_garansi implements Serializable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false, length = 20)
    private Integer id;
    
    @Column(name = "no_klaim", unique = true, nullable = false, length = 20)
    private String no_klaim;
    
    @Column(name = "tanggal")
    private Timestamp tanggal;
    
    @OneToOne
    @JoinColumn(name="no_faktur")
    private penjualan no_faktur;
        
    @OneToOne
    @JoinColumn(name="id_pelanggan")
    private pelanggan kode_pelanggan;
    
    
    @OneToOne
    @JoinColumn(name="id_barang")
    private barang kode_barang;
        
    @Column(name = "jumlah")
    private Long jumlah;

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


    public penjualan getNo_faktur() {
        return no_faktur;
    }

    public void setNo_faktur(penjualan no_faktur) {
        this.no_faktur = no_faktur;
    }

    public String getNo_klaim() {
        return no_klaim;
    }

    public void setNo_klaim(String no_klaim) {
        this.no_klaim = no_klaim;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
