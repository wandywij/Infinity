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
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToOne;
/**
 *
 * @author ade
 */
@Entity
@Table(name = "detail_penjualan")
public class detail_penjualan implements Serializable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "kode_detail_penjualan", unique = true, nullable = false)
    private Long kode_detail_penjualan;
    
    @ManyToOne
    @JoinColumn(name="no_faktur", nullable = true)
    private penjualan no_faktur;
    
    @OneToOne
    @JoinColumn(name="id_barang")
    private barang kode_barang;
    
    @Column(name = "nama_barang")
    private String nama_barang;
    
    @Column(name = "jumlah")
    private Long jumlah;
    
    
    @Column(name = "harga")
    private Long harga;
    
    @Column(name = "diskon")
    private Integer diskon;
    
    @Column(name = "total")
    private Long total;

    public Long getJumlah() {
        return jumlah;
    }

    public void setJumlah(Long jumlah) {
        this.jumlah = jumlah;
    }

    public barang getKode_barang() {
        return kode_barang;
    }

    public void setKode_barang(barang kode_barang) {
        this.kode_barang = kode_barang;
    }


    public String getNama_barang() {
        return nama_barang;
    }

    public void setNama_barang(String nama_barang) {
        this.nama_barang = nama_barang;
    }

    public Integer getDiskon() {
        return diskon;
    }

    public void setDiskon(Integer diskon) {
        this.diskon = diskon;
    }

    public Long getHarga() {
        return harga;
    }

    public void setHarga(Long harga) {
        this.harga = harga;
    }

    public Long getKode_detail_penjualan() {
        return kode_detail_penjualan;
    }

    public void setKode_detail_penjualan(Long kode_detail_penjualan) {
        this.kode_detail_penjualan = kode_detail_penjualan;
    }

    public penjualan getNo_faktur() {
        return no_faktur;
    }

    public void setNo_faktur(penjualan no_faktur) {
        this.no_faktur = no_faktur;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
    
    
}
