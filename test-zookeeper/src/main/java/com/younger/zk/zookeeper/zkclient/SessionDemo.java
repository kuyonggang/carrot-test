/**
 * 
 */
package com.younger.zk.zookeeper.zkclient;

import org.I0Itec.zkclient.ZkClient;

/**
 * 类名称：SessionDemo<br>
 * 类描述：<br>
 * 创建时间：Mar 5, 20199:34:01 PM<br>
 *
 * @author Younger
 * @version	1.0.0
*/
public class SessionDemo {
    
    private final static String CONNECTSTRING = "localhost:2181";

    public static void main(String[] args) {
        ZkClient zkClient = new ZkClient(CONNECTSTRING, 5000);
        System.out.println(zkClient + "-> success");

    }

}
