
public class CsvAdvancedDO {

    byte[] bytes;
    Settings settings;
    public CsvAdvancedDO(){
        settings = new Settings();
    }

    public byte[] getBytes() {
        return bytes;
    }

    //public void setBytes(byte[] bytes) {
    //    this.bytes = bytes;
    //}
    public void setBytes(String bytes) {
        this.bytes = bytes.getBytes();
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public class Settings {

        public Settings() {
            column1 = "";
            column2 = "";
            column3 = "";
            column4 = "";
            column5 = "";
            column6 = "";
            csvDelimiter = ";";
            encoding = "UTF-8";
            isheader = false;
            stringDelimiter = "\"";
        }

        String csvDelimiter;
        String encoding;
        boolean isheader;
        String stringDelimiter;
        String column1 = "";
        String column2 = "";
        String column3 = "";
        String column4 = "";
        String column5 = "";
        String column6 = "";

        Map<String, Integer> columnMap = new HashMap();

        public Integer getMap(String key) {
            if (columnMap.containsKey(key)) {
                return columnMap.get(key);
            } else {
                return -1;
            }
        }

        public String getCsvDelimiter() {
            return csvDelimiter;
        }

        public void setCsvDelimiter(String csvDelimiter) {
            this.csvDelimiter = csvDelimiter;
        }

        public String getEncoding() {
            return encoding;
        }

        public void setEncoding(String encoding) {
            this.encoding = encoding;
        }

        public boolean isIsheader() {
            return isheader;
        }

        public void setIsheader(boolean isheader) {
            this.isheader = isheader;
        }

        public String getStringDelimiter() {
            return stringDelimiter;
        }

        public void setStringDelimiter(String stringDelimiter) {
            this.stringDelimiter = stringDelimiter;
        }

        public String getColumn1() {
            return column1;
        }

        public void setColumn1(String column1) {
            if (column1 != null) {
                this.column1 = column1;
                settings.columnMap.put(column1, 0);
            }
        }

        public String getColumn2() {
            return column2;
        }

        public void setColumn2(String column2) {
            if (column2 != null) {
                this.column2 = column2;
                settings.columnMap.put(column2, 1);
            }
        }

        public String getColumn3() {
            return column3;
        }

        public void setColumn3(String column3) {
            if (column3 != null) {
                this.column3 = column3;
                settings.columnMap.put(column3, 2);
            }
        }

        public String getColumn4() {
            return column4;
        }

        public void setColumn4(String column4) {
            if (column4 != null) {
                this.column4 = column4;
                settings.columnMap.put(column4, 3);
            }
        }

        public String getColumn5() {
            return column5;
        }

        public void setColumn5(String column5) {
            if (column5 != null) {
                this.column5 = column5;
                settings.columnMap.put(column5, 4);
            }
        }

        public String getColumn6() {
            return column6;
        }

        public void setColumn6(String column6) {
            if (column6 != null) {
                this.column6 = column6;
                settings.columnMap.put(column6, 5);
            }
        }

    }

}
