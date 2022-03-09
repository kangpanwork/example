package com.sanri.test.jgit;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.*;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.junit.Test;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * https://github.com/centic9/jgit-cookbook
 */
@Slf4j
public class GitMain {

    private File localGitDir = new File("D:\\project\\sanri-tools-maven");
//    private File localGitDir = new File("D:\\project\\company\\scp-st-messagecenter-component");

    /**
     *
     * @throws IOException
     */
    @Test
    public void currentBranch() throws IOException {
        Git open = Git.open(localGitDir);
        Repository repository = open.getRepository();
        String branch = repository.getBranch();
        System.out.println(branch);
    }

    @Test
    public void listBranchs() throws IOException, GitAPIException {
        Git open = Git.open(localGitDir);

        // git branch
        List<Ref> call = open.branchList().call();
        Iterator<Ref> iterator = call.iterator();
        while (iterator.hasNext()){
            Ref ref = iterator.next();
            System.out.println("Branch: " + ref + " " + ref.getName() + " " + ref.getObjectId().getName());
        }

        // git branch -a
        System.out.println("\n------------- includes remote branchs in here:\n");
        call = open.branchList().setListMode(ListBranchCommand.ListMode.ALL).call();
        Iterator<Ref> refIterator = call.iterator();
        while (refIterator.hasNext()){
            Ref ref = refIterator.next();
            System.out.println("Branch: " + ref + " " + ref.getName() + " " + ref.getObjectId().getName());
        }
    }

    /**
     * 切换分支 ,前提是本地分支必须存在
     * git checkout nacos
     * @throws IOException
     * @throws GitAPIException
     */
    @Test
    public void checkout() throws IOException, GitAPIException {
        Git git = Git.open(localGitDir);
        String branchName = "nacos";
        git.checkout().setCreateBranch(false).setName(branchName).call();
    }

    /**
     * 切换远程 dev 分支
     * git checkout -b dev origin/dev
     * @throws IOException
     * @throws GitAPIException
     */
    @Test
    public void remoteCheckout() throws IOException, GitAPIException {
        Git git = Git.open(localGitDir);
        String branchName = "dev";
        git.checkout().setCreateBranch(true).setName(branchName).call();
    }

    /**
     * 这个使用用户名密码还是验证失败,不知道要怎么设置 key 验证
     * @throws IOException
     * @throws GitAPIException
     *
     * git pull
     */
    @Test
    public void pull() throws IOException, GitAPIException {
        Git git = Git.open(localGitDir);
        UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider =  null;
        git.pull()
                .setCredentialsProvider(usernamePasswordCredentialsProvider)
                .call();
    }

    /**
     * 使用上面的切换分支,切到指定分支 ,然后添加文件,进行提交
     */
    @Test
    public void commit() throws IOException, GitAPIException {
        Git git = Git.open(localGitDir);

        // git add
        DirCache testfile = git.add().addFilepattern("testfile").setUpdate(true).call();

        // git commit
        git.commit().setMessage("提交注释").call();
    }

    /**
     * git push origin dev
     * @throws IOException
     * 这个没有做验证,暂不知道怎么使用,以后补上来
     */
    @Test
    public void push() throws IOException, GitAPIException {
        Git git = Git.open(localGitDir);
        git.push().setRemote("").call();
    }

    /**
     * git tag
     *
     * @throws IOException
     * @throws GitAPIException
     */
    @Test
    public void tagsAndTagCommits() throws IOException, GitAPIException {
        Git git = Git.open(localGitDir);
        Repository repository = git.getRepository();

        List<Ref> call = git.tagList().call();
        for (Ref ref : call) {
            System.out.println("Tag: " + ref.getName() + " " + ref.getObjectId().getName());

            LogCommand log = git.log();
            Ref peelRef = repository.getRefDatabase().peel(ref);
            // peelRef ???
            ObjectId peeledObjectId = peelRef.getPeeledObjectId();
//            System.out.println(peeledObjectId+" |||||||");
            if(peeledObjectId != null){
                log.add(peeledObjectId);
            }else{
                log.add(ref.getObjectId());
            }
            Iterable<RevCommit> revCommits = log.call();
            for (RevCommit revCommit : revCommits) {
                System.out.println("\t\t Commit: " + revCommit /* + ", name: " + rev.getName() + ", id: " + rev.getId().getName() */);
            }

//            break;
        }
    }

    @Test
    public void logs() throws IOException, GitAPIException {
        Git git = Git.open(localGitDir);
        Repository repository = git.getRepository();

        Iterable<RevCommit> logs = git.log()
                .call();
        int count = 0;
        for (RevCommit rev : logs) {
            //System.out.println("Commit: " + rev /* + ", name: " + rev.getName() + ", id: " + rev.getId().getName() */);
            count++;
        }
        System.out.println("Had " + count + " commits overall on current branch");

        logs = git.log()
                .add(repository.resolve("remotes/origin/testbranch"))
                .call();
        count = 0;
        for (RevCommit rev : logs) {
            System.out.println("Commit: " + rev /* + ", name: " + rev.getName() + ", id: " + rev.getId().getName() */);
            count++;
        }
        System.out.println("Had " + count + " commits overall on test-branch");

        logs = git.log()
                .not(repository.resolve("master"))
                .add(repository.resolve("remotes/origin/testbranch"))
                .call();
        count = 0;
        for (RevCommit rev : logs) {
            System.out.println("Commit: " + rev /* + ", name: " + rev.getName() + ", id: " + rev.getId().getName() */);
            count++;
        }
        System.out.println("Had " + count + " commits only on test-branch");

        logs = git.log()
                .all()
                .call();
        count = 0;
        for (RevCommit rev : logs) {
            //System.out.println("Commit: " + rev /* + ", name: " + rev.getName() + ", id: " + rev.getId().getName() */);
            count++;
        }
        System.out.println("Had " + count + " commits overall in repository");

        logs = git.log()
                // for all log.all()
                .addPath("README.md")
                .call();
        count = 0;
        for (RevCommit rev : logs) {
            //System.out.println("Commit: " + rev /* + ", name: " + rev.getName() + ", id: " + rev.getId().getName() */);
            count++;
        }
        System.out.println("Had " + count + " commits on README.md");

        logs = git.log()
                // for all log.all()
                .addPath("pom.xml")
                .call();
        count = 0;
        for (RevCommit rev : logs) {
            //System.out.println("Commit: " + rev /* + ", name: " + rev.getName() + ", id: " + rev.getId().getName() */);
            count++;
        }
        System.out.println("Had " + count + " commits on pom.xml");

        // all commits
        Iterable<RevCommit> call = git.log().all().call();
    }

    @Test
    public void diff() throws IOException, GitAPIException {
        Git git = Git.open(localGitDir);
        Repository repository = git.getRepository();

        listDiff(repository,git,"f932d5c4187694f463285686afb548989e9c9b9d","8263413f7d938331bc91652f603560f8124ca204");
    }

    private static void listDiff(Repository repository, Git git, String oldCommit, String newCommit) throws GitAPIException, IOException {
        final List<DiffEntry> diffs = git.diff()
                .setOldTree(prepareTreeParser(repository, oldCommit))
                .setNewTree(prepareTreeParser(repository, newCommit))
                .call();

        System.out.println("Found: " + diffs.size() + " differences");
        for (DiffEntry diff : diffs) {
            System.out.println("Diff: " + diff.getChangeType() + ": " +
                    (diff.getOldPath().equals(diff.getNewPath()) ? diff.getNewPath() : diff.getOldPath() + " -> " + diff.getNewPath()));
        }
    }

    private static AbstractTreeIterator prepareTreeParser(Repository repository, String objectId) throws IOException {
        // from the commit we can build the tree which allows us to construct the TreeParser
        //noinspection Duplicates
        try (RevWalk walk = new RevWalk(repository)) {
            RevCommit commit = walk.parseCommit(repository.resolve(objectId));
            RevTree tree = walk.parseTree(commit.getTree().getId());

            CanonicalTreeParser treeParser = new CanonicalTreeParser();
            try (ObjectReader reader = repository.newObjectReader()) {
                treeParser.reset(reader, tree.getId());
            }

            walk.dispose();

            return treeParser;
        }
    }

    /**
     * sanritoolsmaven 生成更新包
     */
    @Test
    public void generateUpdatePackage () throws IOException, GitAPIException, InterruptedException {
        // 先执行 mvn clean compile
//        ProcessBuilder processBuilder = new ProcessBuilder();
//        processBuilder.command("cmd", "/c", "mvn", "-DskipTests=true", "clean", "compile", "-f", "pom.xml");
//        Process process = processBuilder.start();
//        process.waitFor();

        // 用于记录补丁包
        File patch = new File("d:/test/patch"+System.currentTimeMillis());patch.mkdirs();
        System.out.println("补丁包路径:"+patch);
        File updateEntry = new File(patch,"updateEntry");
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(updateEntry),"utf-8");

        Git git = Git.open(localGitDir);
        Repository repository = git.getRepository();
        final List<DiffEntry> diffs = git.diff()
                .setOldTree(prepareTreeParser(repository, "306d76760d2b5e789b09f8e8815988917448ed69"))      // 这个是在线更新版本, 以这个为基准
                .setNewTree(prepareTreeParser(repository, ""))
                .call();

        for (DiffEntry diff : diffs) {
            DiffEntry.ChangeType changeType = diff.getChangeType();
            String newPath = diff.getNewPath();
            String oldPath = diff.getOldPath();

            try {
                File changes = new File(patch, "changes");
                if(changeType == DiffEntry.ChangeType.ADD) {
                    copyPatchFile(changeType,changes, newPath,outputStreamWriter);
                }else if(changeType == DiffEntry.ChangeType.MODIFY || changeType == DiffEntry.ChangeType.DELETE){
                    copyPatchFile(changeType,changes,oldPath,outputStreamWriter);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        outputStreamWriter.close();
    }

    /**
     * 复制补丁包文件
     * @param changeType
     * @param patch
     * @param relativePath
     * @param outputStreamWriter
     */
    private void copyPatchFile(DiffEntry.ChangeType changeType, File patch, String relativePath, OutputStreamWriter outputStreamWriter) throws IOException {
        log.info("[{}]补丁文件[{}]",changeType,relativePath);

        File classes = new File(localGitDir, "target/classes");
        File resourceRoot = new File(localGitDir,"src/main/resources");

        String fileName = new File(relativePath).getName();
        if("pom.xml".equals(fileName)){
            log.info("暂时跳过 pom.xml ,以后需要解析 pom 获取变更记录,得到需要删除和上传的包 ");
            return ;
        }

        final String javaPathPrefix = "src/main/java";
        final String resourcesPathPrefix = "src/main/resources";
        final String webappPathPrefix = "src/main/webapp";

        if (relativePath.startsWith(javaPathPrefix)) {
            // java 文件,取编译后的文件,需要注意内部类的问题
            Path javaPath = Paths.get(javaPathPrefix).relativize(Paths.get(relativePath));
            File sourceFile = new File(classes, javaPath.toString());
            File parentDir = sourceFile.getParentFile();
            String baseName = FilenameUtils.getBaseName(fileName);

            // 写入更新文件
            Path normalize = javaPath.getParent().normalize();
            String classPath = normalize.toString().replaceAll("\\\\", "/");
            outputStreamWriter.write(changeType + " WEB-INF/classes/"+classPath+"/"+baseName+".class" + '\n');
            // 如果为删除,则不需要复制文件
            if(changeType == DiffEntry.ChangeType.DELETE){
                return ;
            }

            // 找到所有编译后的类,包含内部类
            WildcardFileFilter wildcardFileFilter = new WildcardFileFilter(baseName+"$*.class");
            OrFileFilter orFileFilter = new OrFileFilter(wildcardFileFilter, new NameFileFilter(baseName+".class"));
            Collection<File> files = FileUtils.listFiles(parentDir, orFileFilter, TrueFileFilter.TRUE);

            // 复制到补丁包目录,按目录复制
            File targetDir = new File(patch, "WEB-INF/classes");
            File classFilesDir = new File(targetDir, javaPath.getParent().toString());
            classFilesDir.mkdirs();
            for (File file : files) {
                FileUtils.copyFile(file, new File(classFilesDir, file.getName()));
            }

            return ;
        }
        if(relativePath.startsWith(resourcesPathPrefix)){
            // 复制资源文件
            Path resourcesPath = Paths.get(resourcesPathPrefix).relativize(Paths.get(relativePath));
            File sourceFile = new File(classes, resourcesPath.toString());

            // 写入更新文件
            Path normalize = resourcesPath.getParent().normalize();
            String classPath = normalize.toString().replaceAll("\\\\", "/");
            outputStreamWriter.write(changeType + " WEB-INF/classes/"+classPath+"/"+fileName + '\n');

            // 如果为删除,则不需要复制文件
            if(changeType == DiffEntry.ChangeType.DELETE){
                return ;
            }

            // 创建目标路径
            File targetDir = new File(patch, "WEB-INF/classes");
            File targetFilePath = new File(targetDir, resourcesPath.getParent().toString());
            targetFilePath.mkdirs();

            // 复制文件到目标路径
            FileUtils.copyFile(sourceFile,new File(targetFilePath,sourceFile.getName()));

            return ;
        }

        if(relativePath.startsWith(webappPathPrefix)){
            // 复制 webapp 文件
            File sourceFile = new File(localGitDir, relativePath);

            // 创建目标路径
            Path webappPath = Paths.get(webappPathPrefix).relativize(Paths.get(relativePath));
            File targetPath = new File(patch, webappPath.getParent().toString());targetPath.mkdirs();

            // 写入更新文件
            outputStreamWriter.write(changeType+" "+webappPath + '\n');
            // 如果为删除,则不需要复制文件
            if(changeType == DiffEntry.ChangeType.DELETE){
                return ;
            }

            // 复制文件
            FileUtils.copyFile(sourceFile,new File(targetPath,fileName));

            return ;
        }

        log.warn("当前文件[{}]不受支持补丁,或未找到",relativePath);
    }

    @Test
    public void test(){
        File file = new File("D:\\project\\sanri-tools-maven\\target\\classes\\com\\sanri\\app\\servlet");
        WildcardFileFilter wildcardFileFilter = new WildcardFileFilter("CompanyServlet$*.class");
        OrFileFilter orFileFilter = new OrFileFilter(wildcardFileFilter, new NameFileFilter("CompanyServlet.class"));
        Collection<File> files = FileUtils.listFiles(file, orFileFilter, TrueFileFilter.TRUE);
        for (File file1 : files) {
            System.out.println(file1);
        }
    }

    @Test
    public void pathTest(){
        String path = "\\project\\sanri-tools-maven\\target\\classes\\com\\sanri\\app\\servle";
        System.out.println(Paths.get(path).toString().replaceAll("\\\\","/"));
    }

    /**
     * 编译不成功,不知道什么原因,使用 idea 的工具可以; 第三方包找不到
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testMvn() throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        File file = new File(localGitDir, "pom.xml");
        File settting = new File("C:\\Users\\091795960\\.m2/settings.xml");
        processBuilder.command("cmd", "/c", "mvn","-s",settting.getAbsolutePath(), "-DskipTests=true", "clean", "compile", "-f", file.getAbsolutePath());
        Process process = processBuilder.start();
        BufferedReader inputStreamReader = new BufferedReader(new InputStreamReader(process.getInputStream(),"utf-8"));
        new Thread(){
            @Override
            public void run() {
                String line = null;
                try {
                    while ((line = inputStreamReader.readLine()) != null){
                        System.out.println(line);
                    }
                    inputStreamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        int waitFor = process.waitFor();
        process.destroy();
    }
}
