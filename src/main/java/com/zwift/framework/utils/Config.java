package com.zwift.framework.utils;

/**
 * Created by DHOLMAN on 1/26/16.
 */

import org.testng.Reporter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import static org.testng.Assert.fail;


// handles the parsing of the xxx.config file used in data driven test cases
public class Config {

    String str, key;

    private String filePath = "driver.properties";

    public Config(String filePath) {
        try {
            File f = new File(filePath);
            if (!f.exists()) {
                throw new FileNotFoundException("driver.properties not found in the root directory! Please see ReadMe !!! !! !!! !!");
            } else {
                this.filePath = filePath;
            }
        } catch (Exception f) {
            Reporter.log("", true);
            Reporter.log("**********************************************************************************", true);
            Reporter.log(f.getLocalizedMessage(), true);
            Reporter.log("**********************************************************************************", true);
            Reporter.log("", true);
            fail("driver.properties not found in the root directory or could not find the specified .properties file! Please see ReadMe");
        }
    }

    public String ReadProperty(String propkey) {
        String propval = "";
        try {
            int check = 0;
            while (check == 0) {
                check = 1;
                File cfgfile = new File(filePath);
                if (cfgfile.exists()) {
                    Properties props = new Properties();
                    FileInputStream propin = new FileInputStream(cfgfile);
                    props.load(propin);
                    propval = props.getProperty(propkey.toUpperCase());
                } else {
                    check = 0;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return propval;
    }
}