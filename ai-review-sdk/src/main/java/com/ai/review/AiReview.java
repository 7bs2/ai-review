package com.ai.review;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class AiReview {

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println(System.getenv("token"));
        // 获取最近两次提交的 diff
        ProcessBuilder builder = new ProcessBuilder("git", "diff", "HEAD~1", "HEAD");
        builder.directory(new File("."));
        builder.redirectErrorStream(true);
        Process process = builder.start();
        StringBuilder diff = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                diff.append(line).append("\n");
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            System.err.println("git diff 命令失败");
            System.exit(1);
        }
        // 调用 AI 接口（这里只打印 diff，可换成调用 OpenAI）
        System.out.println("最近两次提交的 diff:");
        System.out.println(diff);
    }

}
