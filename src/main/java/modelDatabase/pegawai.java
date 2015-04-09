/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelDatabase;

import java.io.Serializable;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
/**
 *
 * @author ade
 */
@Entity
@Table(name = "pegawai")
public class pegawai implements Serializable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false, length = 20)
    private Integer id;
    
    @Column(name = "id_pegawai", unique = true, nullable = false, length = 20)
    private String id_pegawai;
    
    @Column(name = "nama_pegawai", length = 200)
    private String nama_pegawai;
            
    @Column(name = "alamat_pegawai", length = 200)
    private String alamat_pegawai;
    
    @Column(name = "telepon_pegawai", length = 200)
    private String telepon_pegawai;
    
    @Column(name = "email_pegawai", length = 200)
    private String email_pegawai;
    
    @Column(name = "divisi", length = 50)
    private String divisi;
    
    public pegawai(String id_pegawai) {
        this.id_pegawai = id_pegawai;
    }
    
    public pegawai(){
        
    }

    public String getAlamat_pegawai() {
        return alamat_pegawai;
    }

    public void setAlamat_pegawai(String alamat_pegawai) {
        this.alamat_pegawai = alamat_pegawai;
    }

    public String getEmail_pegawai() {
        return email_pegawai;
    }

    public void setEmail_pegawai(String email_pegawai) {
        this.email_pegawai = email_pegawai;
    }

    public String getId_pegawai() {
        return id_pegawai;
    }

    public void setId_pegawai(String id_pegawai) {
        this.id_pegawai = id_pegawai;
    }


    public String getNama_pegawai() {
        return nama_pegawai;
    }

    public void setNama_pegawai(String nama_pegawai) {
        this.nama_pegawai = nama_pegawai;
    }

    public String getTelepon_pegawai() {
        return telepon_pegawai;
    }

    public void setTelepon_pegawai(String telepon_pegawai) {
        this.telepon_pegawai = telepon_pegawai;
    }

    public String getDivisi() {
        return divisi;
    }

    public void setDivisi(String divisi) {
        this.divisi = divisi;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
 
    
}
