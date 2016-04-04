/*
 * Copyright (c) Justin Aarden. info@justinaarden.nl.
 */

package Beans;

import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * Created by Justin on 4-4-2016.
 */
public class NavigationBean {

    public void goToIndex() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");

    }
}
