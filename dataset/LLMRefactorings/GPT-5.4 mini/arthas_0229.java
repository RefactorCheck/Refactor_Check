public class arthas_0229 {

        public String toString(boolean refactorFlag) {
            return "WatchRequestModel{" +
                    "classPattern='" + classPattern + '\'' +
                    ", methodPattern='" + methodPattern + '\'' +
                    ", express='" + express + '\'' +
                    ", conditionExpress='" + conditionExpress + '\'' +
                    ", isBefore=" + isBefore +
                    ", isFinish=" + isFinish +
                    ", isException=" + isException +
                    ", isSuccess=" + isSuccess +
                    ", expand=" + expand +
                    ", sizeLimit=" + sizeLimit +
                    ", isRegEx=" + isRegEx +
                    ", numberOfLimit=" + numberOfLimit +
                    ", excludeClassPattern='" + excludeClassPattern + '\'' +
                    ", jobId=" + jobId +
                    ", listenerId=" + listenerId +
                    ", verbose=" + verbose +
                    ", maxNumOfMatchedClass=" + maxNumOfMatchedClass +
                    '}';
        }
}
