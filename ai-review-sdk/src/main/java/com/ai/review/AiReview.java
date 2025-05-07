package com.ai.review;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class AiReview {

    public static void main(String[] args) throws IOException, InterruptedException {
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

        // 示例 AI 审查输出（真实项目中请调用 API）
        System.out.println("AI Review 建议:");
        System.out.println("TODO: 这里你可以调用 OpenAI 接口来分析 diff 并输出审查建议");

    }

}
