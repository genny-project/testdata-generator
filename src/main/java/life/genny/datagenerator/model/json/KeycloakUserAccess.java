package life.genny.datagenerator.model.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class KeycloakUserAccess implements Serializable {
    @JsonProperty("manageGroupMembership")
    private boolean manageGroupMembership;
    @JsonProperty("view")
    private boolean view;
    @JsonProperty("mapRoles")
    private boolean mapRoles;
    @JsonProperty("impersonate")
    private boolean impersonate;
    @JsonProperty("manage")
    private boolean manage;

    public KeycloakUserAccess() {
    }

    public boolean isManageGroupMembership() {
        return manageGroupMembership;
    }

    public void setManageGroupMembership(boolean manageGroupMembership) {
        this.manageGroupMembership = manageGroupMembership;
    }

    public boolean isView() {
        return view;
    }

    public void setView(boolean view) {
        this.view = view;
    }

    public boolean isMapRoles() {
        return mapRoles;
    }

    public void setMapRoles(boolean mapRoles) {
        this.mapRoles = mapRoles;
    }

    public boolean isImpersonate() {
        return impersonate;
    }

    public void setImpersonate(boolean impersonate) {
        this.impersonate = impersonate;
    }

    public boolean isManage() {
        return manage;
    }

    public void setManage(boolean manage) {
        this.manage = manage;
    }
}
