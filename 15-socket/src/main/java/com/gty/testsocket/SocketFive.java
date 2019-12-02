package com.gty.testsocket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 客户端向服务端发送对象
 */
public class SocketFive {

    /*===========================实体类开始===============================*/
    static class User implements Serializable{
        private static final long serialVersionUID = 5321738719161973699L;
        private String name;
        private Integer age;

        public User(String name, Integer age) {
            this.name = name;
            this.age = age;
        }
        @Override
        public String toString() {
            return "User{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }
    /*============================实体类结束==============================*/

    /*=============================服务端开始========================*/
    static class Service {
        public static void main(String[] args) throws IOException, ClassNotFoundException {
            int port = 8888;
            ServerSocket serverSocket = new ServerSocket(port);

            System.out.println("该服务会一直阻塞等待连接");
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            User user =(User) objectInputStream.readObject();

            System.out.println(user.toString());

            inputStream.close();
            socket.close();
            serverSocket.close();
        }
    }
    /*========================服务端结束==================================*/

    /*=======================客户端开始===================================*/
    static class Client {
        public static void main(String[] args) throws IOException {
            String hostname = "127.0.0.1";
            int port = 8888;
            Socket socket = new Socket(hostname, port);
            OutputStream outputStream = socket.getOutputStream();
            User user = new User("测试传输对象", 100);
            //对象必须实现序列化
            //java.io.NotSerializableException: domain.User
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(user);

            outputStream.close();
            objectOutputStream.close();
            socket.close();
        }
    }
    /*=======================客户端结束==========================*/
}
