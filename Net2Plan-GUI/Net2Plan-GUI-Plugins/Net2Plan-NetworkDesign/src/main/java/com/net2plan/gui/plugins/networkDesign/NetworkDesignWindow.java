/*******************************************************************************
 * Copyright (c) 2017 Pablo Pavon Marino and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the 2-clause BSD License 
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/BSD-2-Clause
 *
 * Contributors:
 *     Pablo Pavon Marino and others - initial API and implementation
 *******************************************************************************/
package com.net2plan.gui.plugins.networkDesign;

/**
 * Created by Jorge San Emeterio on 7/03/17.
 */
public enum NetworkDesignWindow
{
    network(NetworkDesignWindow.networkWindowName),
    whatif(NetworkDesignWindow.whatifWindowName);

    private final static String networkWindowName = "View/Edit cloud state";
    private final static String whatifWindowName = "About it";

    private final String text;

    NetworkDesignWindow(final String text)
    {
        this.text = text;
    }

    public static NetworkDesignWindow parseString(final String text)
    {
        switch (text)
        {
            case NetworkDesignWindow.networkWindowName:
                return network;
            case NetworkDesignWindow.whatifWindowName:
                return whatif;

        }

        return null;
    }

    public static String getWindowName(final NetworkDesignWindow tab)
    {
        switch (tab)
        {
            case network:
                return NetworkDesignWindow.networkWindowName;
            case whatif:
                return NetworkDesignWindow.whatifWindowName;

        }

        return null;
    }
}
