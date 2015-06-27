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
@Table(name = "retur_pembelian_detail")
public class retur_pembelian_detail implements Serializable{
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
    @JoinColumn(name = "no_returpembelian", nullable = true)
    private retur_pembelian no_returpembelian;

    public retur_pembelian getReturPembelian() {
        return no_returpembelian;
    }

    public void setReturPembelian(retur_pembelian returPembelian) {
        this.no_returpembelian = returPembelian;
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
