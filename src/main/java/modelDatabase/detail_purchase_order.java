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
@Table(name = "detail_purchase_order")
public class detail_purchase_order implements Serializable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "kode_detail_po", unique = true, nullable = false)
    private Long kode_detail_po;
    
    @ManyToOne
    @JoinColumn(name="no_po", nullable = true)
    private purchase_order no_po;
    
    @OneToOne
    @JoinColumn(name="id_barang")
    private barang kode_barang;
    
    @Column(name = "nama_barang")
    private String nama_barang;
    
    @Column(name = "jumlah")
    private Long jumlah;

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

    public Long getKode_detail_po() {
        return kode_detail_po;
    }

    public void setKode_detail_po(Long kode_detail_po) {
        this.kode_detail_po = kode_detail_po;
    }


    public String getNama_barang() {
        return nama_barang;
    }

    public void setNama_barang(String nama_barang) {
        this.nama_barang = nama_barang;
    }

    public purchase_order getNo_po() {
        return no_po;
    }

    public void setNo_po(purchase_order no_po) {
        this.no_po = no_po;
    }

}
