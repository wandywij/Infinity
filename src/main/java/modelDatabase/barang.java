/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelDatabase;

import java.io.Serializable;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
/**
 *
 * @author ade
 */
@Entity
@Table(name = "barang")
public class barang implements Serializable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false, length = 20)
    private Integer id;
    
    @Column(name = "kode_barang", unique = true, nullable = false, length = 20)
    private String kode_barang;
    
    @Column(name = "nama_barang", length = 200)
    private String nama_barang;
            
    @Column(name = "harga")
    private Long harga;
    
    @Column(name = "jumlah_stok")
    private Long jumlah_stok;
    
    public barang() {
        
    }
    
    public barang(String kode_barang) {
        this.kode_barang = kode_barang;
    }

    public Long getHarga() {
        return harga;
    }

    public void setHarga(Long harga) {
        this.harga = harga;
    }


    public String getKode_barang() {
        return kode_barang;
    }

    public void setKode_barang(String kode_barang) {
        this.kode_barang = kode_barang;
    }

    public String getNama_barang() {
        return nama_barang;
    }

    public void setNama_barang(String nama_barang) {
        this.nama_barang = nama_barang;
    }

    public Long getJumlah_stok() {
        return jumlah_stok;
    }

    public void setJumlah_stok(Long jumlah_stok) {
        this.jumlah_stok = jumlah_stok;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
   
}
