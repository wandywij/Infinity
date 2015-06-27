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
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.OneToMany;

/**
 *
 * @author ade
 */
@Entity
@Table(name = "retur_pembelian")
public class retur_pembelian{
    
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false, length = 20)
    private Integer id;
    
    @Id
    @Column(name = "no_returpembelian", unique = true, nullable = false, length = 20)
    private String no_returpembelian;
    
    @Column(name = "tanggal")
    private Timestamp tanggal;
    
//    @OneToOne
//    @JoinColumn(name="id_po")
    @Column(name="no_po", unique = true, nullable = false, length = 20)
    private String no_po;
    
    @OneToOne
    @JoinColumn(name="id_pegawai")
    private pegawai id_pegawai;
    
//    @OneToOne
//    @JoinColumn(name="id_supplier")
//    private supplier kode_supplier;
    
    
//    @OneToOne
//    @JoinColumn(name="id_barang")
//    private barang kode_barang;

    public pegawai getId_pegawai() {
        return id_pegawai;
    }

    public void setId_pegawai(pegawai id_pegawai) {
        this.id_pegawai = id_pegawai;
    }
    
    public List<retur_pembelian_detail> getReturPenjualanDetail() {
        return retur_pembelian_detail;
    }

    public void setReturPenjualanDetail(List<retur_pembelian_detail> kode_barang) {
        this.retur_pembelian_detail = kode_barang;
    }
    
    @OneToMany(mappedBy="no_returpembelian", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<retur_pembelian_detail> retur_pembelian_detail;
//    public barang getKode_barang() {
//        return kode_barang;
//    }
//
//    public void setKode_barang(barang kode_barang) {
//        this.kode_barang = kode_barang;
//    }

//    public supplier getKode_supplier() {
//        return kode_supplier;
//    }
//
//    public void setKode_supplier(supplier kode_supplier) {
//        this.kode_supplier = kode_supplier;
//    }

    public String getNo_po() {
        return no_po;
    }

    public void setNo_po(String no_po) {
        this.no_po = no_po;
    }

    public String getNo_retur_pembelian() {
        return no_returpembelian;
    }

    public void setNo_retur_pembelian(String no_retur_pembelian) {
        this.no_returpembelian = no_retur_pembelian;
    }

    public Timestamp getTanggal() {
        return tanggal;
    }

    public void setTanggal(Timestamp tanggal) {
        this.tanggal = tanggal;
    }
    
    
    
}
