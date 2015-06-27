/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelDatabase;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author wandy
 */

@Entity
@Table(name = "retur_penjualan_detail")
public class retur_penjualan_detail implements Serializable{
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false, length = 20)
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    
    @ManyToOne
    @JoinColumn(name = "no_returpenjualan", nullable = true)
    private retur_penjualan no_returpenjualan;

    public retur_penjualan getReturPenjualan() {
        return no_returpenjualan;
    }

    public void setReturPenjualan(retur_penjualan returPenjualan) {
        this.no_returpenjualan = returPenjualan;
    }
    
    @OneToOne
    @JoinColumn(name="id_barang")
    private barang kode_barang;
    
    @Column(name = "jumlah")
    private int jumlah;


    public barang getKode_barang() {
        return kode_barang;
    }

    public void setKode_barang(barang kode_barang) {
        this.kode_barang = kode_barang;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }
    
    
}
