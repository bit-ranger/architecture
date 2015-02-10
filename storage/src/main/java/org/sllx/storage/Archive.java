package org.sllx.storage;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Archive implements Serializable{

    private static final long serialVersionUID = -6265714340584955008L;
    
    
    public  Archive(){
        
        
    }
    
    public Archive(String name){
        this.name = name;
    }
    
    private String name;

    @XmlMimeType("application/octet-stream")
    private DataHandler body;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataHandler getBody() {
        return body;
    }

    public void setBody(DataHandler archive) {
        this.body = archive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Archive other = (Archive) o;

        if (body != null ? !body.equals(other.body) : other.body != null ) return false;
        if (name != null ? !name.equals(other.name) : other.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (body != null ? body.hashCode() : 0);
        return result;
    }
}
