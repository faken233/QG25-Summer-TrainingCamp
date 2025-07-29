# **Git   用法**

*1.配置身份：去掉后面的姓名邮件再输入可以查看是否配置成功*
  git config --global user.name "yyz"
  git config --global user.email "2076898599@qq.com"

*2.给某个项目新建代码仓库：*
  先进入该项目的目录: （已进入D盘）cd AndroidStudioProjects/BroadcastBestPractice
  git init；
  创建完成后，会在该目录下生成隐藏的.git文件用来记录本地所有Git操作，可以通过ls -al 来查看
  想要删除本地仓库也只要删除这个文件就行

*3.提交本地代码*
  **add把代码添加进来**
添加文件（夹）：git add 文件（夹）名
添加当前目录所有文件：git add .

  **commit真正执行提交操作**
git commit -m "First commit"
一定要通过-m参数来描述提交信息，否则提交是不合法的

**注意很多地方不能漏掉空格**

**7.5.1 忽略文件**
我们并不想把所有文件都添加到版本控制中，如build目录下的文件是编译自动生成的。

Git仓库会自动读取.gitignore文件（一个在根目录下，一个在app中）把其中指定文件或目录排除在版本控制之外,我们可以对其中进行修改，满足特定需求
添加所有文件：git add .
完成提交：git commit -m "First commit."

**7.5.2  查看修改内容**

在代码修改之后，在Git Bash中输入git status :则会显示出代码发生修改的文件：

如果要看到文件中代码发生的具体修改：git diff

只看某个文件的修改：git diff app/src/main/java/com/example/providertest/MainActivity.java
减号表示删除，加号表示添加

**7.5.3 撤销未提交的修改**

只要还没add .,就可以使用git checkout app/src/main/java/com/example/providertest/MainActuvuty.java对具体某个文件中的修改进行撤销

如果已经add，就应该先取消添加，然后才能撤回提交：
git reset HEAD app/src/main/java/com/example/providertest/MainActivity.java
再用git checkout app/src/main/java/com/example/providertest/MainActivity.java 撤销修改内容

**7.5.4 查看提交记录**

git log

只查看某一条记录：git log 某一条记录的id -1

查看某条记录的具体修改内容：git log 某记录的id -1 -p

一.准备工作：
git init
git add . 
git commit -m "备注"

二.分支用法：
当发布一个版本1.0，建立一个分支，然后1.1版本继续在主分支上进行研发，当1.0出现任何bug时，只需在分支上进行修改，然后发布新的1.0版本，修改后把分支合并到主分支中，就可以避免1.1也出现相同的bug

**查看分支**：git branch （只有在commit之后才会生成master分支）
**创建分支**：git branch 分支名
**切换分支**：git checkout 分支名

把A分支的修改**合并到master分支**上：
先切换到主分支：git checkout master 
再合并：git merge A
可能会出现代码冲突只能自己解决

**删除本地分支**A：git branch -D A

## 三.远程版本库协作

**把版本库的代码下载到本地**：git clone 版本库地址

**同步本地代码到版本库**：git push origin master 
origin指的是版本库的Git地址，master指远程代码库的master分支

把版本库的代码同步到本地：
**用fetch+merge：**
git fetch origin master   同步下来的代码会存放到origin/master分支上，
查看远程版本库修改了哪些东西：git differ origin/master
把origin/master分支和当前分支合并：git merge origin/master

**用pull替代fetch+merge**：
获取远程版本库的master分支最新代码并且合并到本地当前分支：git pull origin master

fatal: refusing to merge unrelated histories以下情况可能会出现这种警告：

1. **首次从远程仓库拉取**：如果你的本地仓库是全新的，而远程仓库已经有了提交历史，Git 默认会拒绝合并这两个没有共同祖先的分支。
2. **合并两个完全不同的仓库**：如果你尝试将两个完全不同的仓库合并，它们之间没有共享的提交历史，Git 也会拒绝合并。

用：git pull origin master --allow-unrelated-histories解决





**添加远程仓库**：git remote add origin 远程仓库地址

 **删除远程仓库**：git remote rm origin

**远程仓库重命名**： git remote [rename](https://so.csdn.net/so/search?q=rename&spm=1001.2101.3001.7020) <old_name> <new_name>

**检查远程仓库是否正确添加**：**git remote -v**
**推送所有分支：**git push -u origin --all
**推送当前分支：**git push -u origin

 在pull之后：会进入编辑器模式

- 在Vim编辑器中，你可以通过按 `Esc` 键确保你不在插入模式，然后输入 `:wq` 并按 `Enter` 键来保存更改并退出编辑器。
- 如果你不想保存合并提交消息并退出，可以输入 `:q!` 并按 `Enter` 键来放弃更改并退出

**查看远程仓库的所有分支列表**：git remote show 远程仓库别名

**删除远程仓库中指定的分支**：git push 远程仓库别名 --delete 远程分支名

把本地的master分支提交到远程代码库的yyz2分支，并且设置yyz2为本地master的上游分支，后续可以直接push和pull(但是git会提醒你本地和远程分支名对不上，最好采用其他推送方式)，无需指定上游分支

```sh
git push -u origin master:yyz2
```

**git push origin yyz2**
Git会尝试将 `yyz2` 分支的所有本地提交推送到远程仓库 `origin` 中的对应分支。如果远程仓库中没有名为 `yyz2` 的分支，Git会创建这个分支并推送你的更改

**版本回退**：git log 查看修改的版本号，找到要撤销的修改
                    git revert 版本号（这就已经在本地进行了回退）
                    git push origin 分支名 （把撤销的结果同步到远程仓库）

