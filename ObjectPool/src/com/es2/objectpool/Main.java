package com.es2.objectpool;

import java.io.IOException;
import java.net.HttpURLConnection;

public class Main {
    public static void main(String[] args) {
        ReusablePool pool = ReusablePool.getInstance();
        pool.setMaxPoolSize(10);

        HttpURLConnection connection = null;
        try {
            connection = pool.acquire();
            System.out.println("Conexao adquirida com sucesso: " + connection.getClass().getSimpleName());
        } catch (PoolExhaustedException e) {
            System.out.println("Pool esgotado.");
        } catch (IOException e) {
            System.out.println("Falha ao abrir conexao: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    pool.release(connection);
                    System.out.println("Conexao devolvida ao pool.");
                } catch (ObjectNotFoundException e) {
                    System.out.println("Conexao nao encontrada no pool durante release.");
                }
            }
            pool.resetPool();
        }
    }
}