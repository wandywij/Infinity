/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelDatabase;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
/**
 *
 * @author ade
 */
@Entity
@Table(name = "penjualan")
public class penjualan implements Serializable  {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false, length = 20)
    private Integer id;
    
    @Column(name = "no_faktur", unique = true, nullable = false, length = 20)
    private String no_faktur;
    
    @Column(name = "tanggal")
    private Timestamp tanggal;
    
    @OneToOne
    @JoinColumn(name="id_pelanggan")
    private pelanggan kode_pelanggan;
        
    @OneToOne
    @JoinColumn(name="id_pegawai")
    private pegawai id_pegawai;
    
    @OneToMany(mappedBy = "no_faktur", cascade =  CascadeType.REMOVE, orphanRemoval = true )
    private List<detail_penjualan> penjualan_detail;
    
    public penjualan(){
        
    }
    
    public penjualan(String no_faktur){
        this.no_faktur = no_faktur;
    }

    public pegawai getId_pegawai() {
        return id_pegawai;
    }

    public void setId_pegawai(pegawai id_pegawai) {
        this.id_pegawai = id_pegawai;
    }

    public pelanggan getKode_pelanggan() {
        return kode_pelanggan;
    }

    public void setKode_pelanggan(pelanggan kode_pelanggan) {
        this.kode_pelanggan = kode_pelanggan;
    }

    public String getNo_faktur() {
        return no_faktur;
    }

    public void setNo_faktur(String no_faktur) {
        this.no_faktur = no_faktur;
    }

    public List<detail_penjualan> getPenjualan_detail() {
        return penjualan_detail;
    }

    public void setPenjualan_detail(List<detail_penjualan> penjualan_detail) {
        this.penjualan_detail = penjualan_detail;
    }

    public Timestamp getTanggal() {
        return tanggal;
    }

    public void setTanggal(Timestamp tanggal) {
        this.tanggal = tanggal;
    }
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
