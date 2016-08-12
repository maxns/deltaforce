package org.namstorm.deltaforce.annotations.processors.util;

import org.apache.velocity.util.StringUtils;

/**
 * Created by maxnam-storm on 12/8/2016.
 *
 * Some utils
 */
public class DFUtil {


    /**
     * @param name
     * @param aliasParam
     * @return
     */
    public static String compileAlias(String name, String aliasParam) {
        /**
         *         #if( ${field.mapItem.substring(0,1)}=="+" )
         #set( $fnInMethod = "${field.alias.substring(0,1).toUpperCase()}${field.alias.substring(1)}${field.mapItem.substring(1)}" )
         #else
         #set ($fnInMethod = "${field.mapItem.substring(0,1).toUpperCase()}${field.mapItem.substring(1)}" )
         #end

         */
        String res = StringUtils.capitalizeFirstLetter(name);

        String[] aliasParts = StringUtils.split(aliasParam==null?"":aliasParam, ";");

        for(String alias:aliasParts) {
            if(alias.startsWith("+")) {
                res = res + StringUtils.capitalizeFirstLetter(alias.substring(1));
            }else if(alias.startsWith("-")) {
                if(res.endsWith(alias.substring(1))) {
                    res = res.substring(0,res.length()-alias.length()+1);
                }
            }else {
                res = StringUtils.capitalizeFirstLetter(alias);
            }

        }
        return res;
    }

}
