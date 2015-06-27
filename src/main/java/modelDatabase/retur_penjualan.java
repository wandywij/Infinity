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
@Table(name = "retur_penjualan")
public class retur_penjualan {
    
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false, length = 20)
    private Integer id;
    
    @Id
    @Column(name = "no_returpenjualan", unique = true, nullable = false, length = 20)
    private String no_returpenjualan;
    
    @Column(name = "tanggal")
    private Timestamp tanggal;
    
//    @OneToOne
//    @JoinColumn(name="no_faktur")
//    private penjualan no_faktur;
    @Column(name="no_faktur", unique = true, nullable = false, length = 20)
    private String no_faktur;
    //private String nomorFaktur;
    
    @OneToOne
    @JoinColumn(name="id_pegawai")
    private pegawai id_pegawai;
    
    
//    @OneToOne
//    @JoinColumn(name="id_pelanggan")
//    private pelanggan kode_pelanggan;
    
    
//    @OneToOne
//    @JoinColumn(name="id_barang")
//    private barang kode_barang;
    
//    @Column(name = "id_barang")
//    private String id_barang;
    
    
    //@OneToMany(mappedBy = "no_faktur", cascade =  CascadeType.REMOVE, orphanRemoval = true )
    //private barang kode_barang;
    
    @OneToMany(mappedBy = "no_returpenjualan", cascade =  CascadeType.REMOVE, orphanRemoval = true )
    private List<retur_penjualan_detail> retur_penjualan_detail;
    

    public pegawai getId_pegawai() {
        return id_pegawai;
    }

    public void setId_pegawai(pegawai id_pegawai) {
        this.id_pegawai = id_pegawai;
    }

    public List<retur_penjualan_detail> getReturPenjualanDetail() {
        return retur_penjualan_detail;
    }

    public void setReturPenjualanDetail(List<retur_penjualan_detail> kode_barang) {
        this.retur_penjualan_detail = kode_barang;
    }


    public Timestamp getTanggal() {
        return tanggal;
    }

    public void setTanggal(Timestamp tanggal) {
        this.tanggal = tanggal;
    }

    public String getNo_faktur() {
        return no_faktur;
    }

    public void setNo_faktur(String no_faktur) {
        this.no_faktur = no_faktur;
    }
    
    
//    public void setNomorFaktur(String nomor_faktur)
//    {
//        nomorFaktur = nomor_faktur;
//    }
//    
//    public String getNomorFaktur()
//    {
//        return nomorFaktur;
//    }

    public String getNo_returpenjualan() {
        return no_returpenjualan;
    }

    public void setNo_returpenjualan(String no_returpenjualan) {
        this.no_returpenjualan = no_returpenjualan;
    }
}
