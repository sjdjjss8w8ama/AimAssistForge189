# AimAssistForge189

Mod para Minecraft Java 1.8.9 / Forge 11.15.1.2318 / Java 8.

## Compilar pelo Android usando GitHub Actions

1. Entre em https://github.com e crie uma conta ou faça login.
2. Toque em **+** > **New repository**.
3. Dê o nome `AimAssistForge189`, marque **Public** ou **Private** e toque em **Create repository**.
4. Na página do repositório, toque em **Add file** > **Upload files**.
5. Extraia este ZIP no celular e envie o conteúdo da pasta do projeto (incluindo a pasta `.github`). Se o Android não mostrar pastas começando com ponto, crie a pasta e o arquivo do workflow pelo site conforme `.github/workflows/build.yml` deste projeto.
6. Toque em **Commit changes**.
7. Abra a aba **Actions**, selecione **Build Minecraft Forge 1.8.9 mod** e toque em **Run workflow** se o build não iniciar automaticamente.
8. Aguarde o indicador verde. Abra a execução concluída, role até **Artifacts** e baixe `AimAssistForge189-jar`.
9. Extraia o ZIP baixado. O mod estará dentro como `AimAssistForge189-1.0.0.jar`.
10. Coloque o JAR em `.minecraft/mods` de uma instalação Minecraft Java 1.8.9 com Forge 11.15.1.2318.

## Compilar localmente
Requer JDK 8 e Gradle 2.14.1:
`gradle setupDecompWorkspace build`

## Escopo funcional
O alvo automático é limitado a mobs (`EntityMob`) em single-player; outros jogadores nunca são selecionados.
As configurações são gravadas em `config/aimassist.json`.
