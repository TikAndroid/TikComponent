# git版本管理流程

## 日常需求开发，开发分支拉取流程 ：

1. 基于dev分支在本地checkout feature分支。

* 例如 : git checkout -b dev_1.0_feature_login
* 分支命名规则 ： ${当前remote分支}\_${版本tag}\_${用途}\_${名称}

2. 将本地开发分支push到remote

* 可以利用android studio可视化工具进行push，鼠标右键 -》 git -》 repository -》 push
* git push -u origin <本地分支名>

3. feature分支开发完毕， 主动merge一次基于的dev分支， 保持跟dev线节点一致， 避免执行feature分支merge到dev分支，冲突过多的情况。
* 例如 ： git merge --no-ff ${基于的dev分支}

4. feature分支开发完毕并经过测试， 请将该分支merge到dev分支
5. feature确定以及merge到主线分支后， 请在remote端删除该分支
* 命令 ：
    * 删除remote分支 ： git push --delete <remote_name> <branch_name>
    * 删除local分支 ： git branch -d <branch_name>

## commit msg提交规范 ：
> 请用git commit -s 命令
[#feature #ui #bug #crash #ui #optimize #reactor #release]具体修改说明
1. ...
2. ...
3. ...

## 常用git命令 ：
* 拉取local分支并与remote分支相关联 ： git checkout --track {remote name}/{branch name}
* 提交代码到remote分支 ： git push origin HEAD:refs/for/{remote name}/{branch name}
* merge命令 ：  git merge --no-ff ${基于的dev分支}

## Tips:
* git可视化工具： SourceTree
* 文本文档可视化编辑commit信息， 跳过vim 进行编辑
    * 安装homebrew
    * 使用homebrew安装gedit ： brew install gedit
    * 在project路径下配置gedit ： 打开隐藏文件.git/config -> 在[core]分类下， 加入 editor = gedit


