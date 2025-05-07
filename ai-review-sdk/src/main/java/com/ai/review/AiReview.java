package com.ai.review;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatCompletion;
import com.openai.models.ChatCompletionCreateParams;

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

        String token = System.getenv("TOKEN");

        String prompt = "请根据一下git diff 信息形成一份代码评审报告\n" + diff;

        OpenAIClient client = OpenAIOkHttpClient.builder()
                .apiKey(token)
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .build();
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .addUserMessage(prompt)
                .model("qwen-plus")
                .build();
        ChatCompletion chatCompletion = client.chat().completions().create(params);
        System.out.println(chatCompletion.choices().get(0).message().content().orElse("无返回内容"));

    }

}
