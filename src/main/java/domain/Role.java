/*
 * Copyright (c) Justin Aarden. info@justinaarden.nl.
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
@Entity
public class Role implements Serializable {
    @Id
    @XmlElement(required = true)
    public String roleID;

    public Role(){}

    public Role(String roleID){
        this.roleID = roleID;
    }

    public String getGroupId(){
        return roleID;
    }

    public String rolesToJson(){
        return "\n{"
                + "\t\"groupId\":\"" + roleID + "\",\n "
                + "\t\"roleID\":\"" + roleID + "\" \n"
                + "}";
    }
    public String groupsToJson(){
        return "\n{"
                + "\t\"groupId\":\"" + roleID + "\",\n "
                + "\t\"roleID\":\"" + roleID + "\" \n"
                + "}";
    }
}
