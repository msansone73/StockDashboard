package br.com.msansone.StockDashboard.schedule;

import br.com.msansone.StockDashboard.process.ExcelProcesser;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class ExcelProcessSchedule {
    Logger logger = Logger.getLogger("ExcelProcessSchedule");
    
    @Autowired
    ExcelProcesser processer;

    @Scheduled(fixedRate = 60000)
    public void processExcelFile() throws IOException {
        logger.info("Iniciando processamento do arquivo excel");
        String filePath = "uploads/";
        File file = getOldestXlsFileInOath(filePath);
        if (file != null) {
            File movedfile = copyFileToProcessedFolder(file);
            List<String[]> data = processer.processFile(movedfile.getPath());
            Map<String, Object> map = processer.processData(data);
            // todo: atualiza db
            processer.updateData(map);

            //copy file to processed fold
            logger.info("Iniciando copia para processed.");




        }
    }
    
    private File getOldestXlsFileInOath(String filePath) {
        File directory = new File(filePath);
        File[] files = directory.listFiles();   
        if (ArrayUtils.isEmpty(files)) {
            return null;
        }
        return files[0];
    }

    private File copyFileToProcessedFolder(File file) throws IOException {
        logger.info("copyFileToProcessedFolder: Iniciando processamento do arquivo excel");
        Path originPath = Paths.get("uploads/"+file.getName());
        Path destinyPath = Paths.get("processed/"+file.getName());
        Files.copy(originPath, destinyPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        logger.info("copyFileToProcessedFolder: Iniciando remoçao de arquivo processado.sucesso");
        Files.delete(originPath);
        logger.info("copyFileToProcessedFolder: Arquivo copiado com sucesso");
        return destinyPath.toFile();

    }
    


}
