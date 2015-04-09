/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelDatabase;

import java.sql.Timestamp;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import java.io.Serializable;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
/**
 *
 * @author ade
 */
@Entity
@Table(name = "purchase_order")
public class purchase_order {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false, length = 20)
    private Integer id;
    
    @Column(name = "no_po", unique = true, nullable = false, length = 20)
    private String no_po;
    
    @Column(name = "tanggal")
    private Timestamp tanggal;
    
    @OneToOne
    @JoinColumn(name="id_supplier")
    private supplier kode_supplier_inpo;
        
    @OneToOne
    @JoinColumn(name="id_pegawai")
    private pegawai id_pegawai_inpo;
    
    @OneToMany(mappedBy = "no_po", cascade =  CascadeType.REMOVE, orphanRemoval = true )
    private List<detail_purchase_order> po_detail;
    
    public purchase_order(){
        
    }
    
    public purchase_order(String no_po){
        this.no_po = no_po;
    }

    public pegawai getId_pegawai_inpo() {
        return id_pegawai_inpo;
    }

    public void setId_pegawai_inpo(pegawai id_pegawai_inpo) {
        this.id_pegawai_inpo = id_pegawai_inpo;
    }

    public supplier getKode_supplier_inpo() {
        return kode_supplier_inpo;
    }

    public void setKode_supplier_inpo(supplier kode_supplier_inpo) {
        this.kode_supplier_inpo = kode_supplier_inpo;
    }


    public String getNo_po() {
        return no_po;
    }

    public void setNo_po(String no_po) {
        this.no_po = no_po;
    }

    public Timestamp getTanggal() {
        return tanggal;
    }

    public void setTanggal(Timestamp tanggal) {
        this.tanggal = tanggal;
    }

    public List<detail_purchase_order> getPo_detail() {
        return po_detail;
    }

    public void setPo_detail(List<detail_purchase_order> po_detail) {
        this.po_detail = po_detail;
    }
    
    
}
