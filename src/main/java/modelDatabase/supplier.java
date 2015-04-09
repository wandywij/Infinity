/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelDatabase;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;


import java.io.Serializable;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
/**
 *
 * @author ade
 */
@Entity
@Table(name = "supplier")
public class supplier {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false, length = 20)
    private Integer id;
    
    @Column(name = "kode_supplier", unique = true, nullable = false, length = 20)
    private String kode_supplier;
    
    @Column(name = "nama_supplier", length = 200)
    private String nama_supplier;
            
    @Column(name = "alamat_supplier", length = 200)
    private String alamat_supplier;
    
    @Column(name = "telepon_supplier", length = 200)
    private String telepon_supplier;
    
    @Column(name = "email_supplier", length = 200)
    private String email_supplier;
    
    public supplier(String kode_supplier) {
        this.kode_supplier = kode_supplier;
    }
    
    public supplier(){        
    }

    public String getAlamat_supplier() {
        return alamat_supplier;
    }

    public void setAlamat_supplier(String alamat_supplier) {
        this.alamat_supplier = alamat_supplier;
    }

    public String getEmail_supplier() {
        return email_supplier;
    }

    public void setEmail_supplier(String email_supplier) {
        this.email_supplier = email_supplier;
    }

    public String getKode_supplier() {
        return kode_supplier;
    }

    public void setKode_supplier(String kode_supplier) {
        this.kode_supplier = kode_supplier;
    }

    public String getNama_supplier() {
        return nama_supplier;
    }

    public void setNama_supplier(String nama_supplier) {
        this.nama_supplier = nama_supplier;
    }

    public String getTelepon_supplier() {
        return telepon_supplier;
    }

    public void setTelepon_supplier(String telepon_supplier) {
        this.telepon_supplier = telepon_supplier;
    }
   
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
