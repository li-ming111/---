package com.xueya.service.impl;

import com.xueya.service.SystemMonitoringService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;
import java.util.Map;

@Service
public class SystemMonitoringServiceImpl implements SystemMonitoringService {

    private static final long MB = 1024 * 1024;

    @Override
    public double getCpuUsage() {
        try {
            // 使用OperatingSystemMXBean获取CPU负载
            java.lang.management.OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
            
            // 尝试使用com.sun.management.OperatingSystemMXBean获取更详细的CPU信息
            if (osBean instanceof com.sun.management.OperatingSystemMXBean) {
                com.sun.management.OperatingSystemMXBean sunOsBean = (com.sun.management.OperatingSystemMXBean) osBean;
                double cpuLoad = sunOsBean.getCpuLoad();
                if (cpuLoad >= 0) {
                    return cpuLoad * 100;
                }
                // 获取进程CPU负载
                double processCpuLoad = sunOsBean.getProcessCpuLoad();
                if (processCpuLoad >= 0) {
                    return processCpuLoad * 100;
                }
            }
            
            // 备用方案：使用系统命令获取CPU使用率
            return getCpuUsageFromCommand();
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    private double getCpuUsageFromCommand() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            Process process;
            
            if (os.contains("win")) {
                // Windows系统
                process = Runtime.getRuntime().exec("wmic cpu get loadpercentage");
            } else {
                // Linux/Mac系统
                process = Runtime.getRuntime().exec("top -bn1 | grep \"Cpu(s)\"");
            }
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (os.contains("win")) {
                    try {
                        return Double.parseDouble(line.trim());
                    } catch (NumberFormatException ignored) {
                    }
                } else {
                    // 解析Linux top命令输出
                    if (line.contains("%us")) {
                        String[] parts = line.split(",");
                        for (String part : parts) {
                            if (part.contains("%us") || part.contains("%Cpu")) {
                                String cpuStr = part.replaceAll("[^0-9.]", "");
                                return Double.parseDouble(cpuStr);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    @Override
    public double getMemoryUsage() {
        try {
            // 使用com.sun.management.OperatingSystemMXBean获取物理内存信息
            java.lang.management.OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
            
            if (osBean instanceof com.sun.management.OperatingSystemMXBean) {
                com.sun.management.OperatingSystemMXBean sunOsBean = (com.sun.management.OperatingSystemMXBean) osBean;
                long totalPhysicalMemory = sunOsBean.getTotalMemorySize();
                long freePhysicalMemory = sunOsBean.getFreeMemorySize();
                long usedPhysicalMemory = totalPhysicalMemory - freePhysicalMemory;
                
                return (double) usedPhysicalMemory / totalPhysicalMemory * 100;
            }
            
            // 备用方案：使用Runtime获取内存信息
            Runtime runtime = Runtime.getRuntime();
            long totalMemory = runtime.totalMemory();
            long freeMemory = runtime.freeMemory();
            long usedMemory = totalMemory - freeMemory;
            
            return (double) usedMemory / totalMemory * 100;
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    @Override
    public double getDiskUsage() {
        try {
            File root = new File("/");
            long totalSpace = root.getTotalSpace();
            long freeSpace = root.getFreeSpace();
            long usedSpace = totalSpace - freeSpace;
            
            if (totalSpace > 0) {
                return (double) usedSpace / totalSpace * 100;
            }
            return 0.0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    @Override
    public long getUptime() {
        return ManagementFactory.getRuntimeMXBean().getUptime();
    }

    @Override
    public Map<String, Object> getSystemLoad() {
        Map<String, Object> loadInfo = new HashMap<>();
        
        try {
            java.lang.management.OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
            
            loadInfo.put("systemLoadAverage", osBean.getSystemLoadAverage());
            loadInfo.put("availableProcessors", osBean.getAvailableProcessors());
            loadInfo.put("osName", osBean.getName());
            loadInfo.put("osVersion", osBean.getVersion());
            loadInfo.put("osArch", osBean.getArch());
            
            if (osBean instanceof com.sun.management.OperatingSystemMXBean) {
                com.sun.management.OperatingSystemMXBean sunOsBean = (com.sun.management.OperatingSystemMXBean) osBean;
                loadInfo.put("committedVirtualMemory", sunOsBean.getCommittedVirtualMemorySize() / MB);
                loadInfo.put("totalPhysicalMemory", sunOsBean.getTotalMemorySize() / MB);
                loadInfo.put("freePhysicalMemory", sunOsBean.getFreeMemorySize() / MB);
                loadInfo.put("totalSwapSpace", sunOsBean.getTotalSwapSpaceSize() / MB);
                loadInfo.put("freeSwapSpace", sunOsBean.getFreeSwapSpaceSize() / MB);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return loadInfo;
    }

    @Override
    public Map<String, Object> getJvmMemoryInfo() {
        Map<String, Object> memoryInfo = new HashMap<>();
        
        try {
            MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
            
            // 堆内存信息
            MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
            memoryInfo.put("heapInit", heapMemoryUsage.getInit() / MB);
            memoryInfo.put("heapUsed", heapMemoryUsage.getUsed() / MB);
            memoryInfo.put("heapCommitted", heapMemoryUsage.getCommitted() / MB);
            memoryInfo.put("heapMax", heapMemoryUsage.getMax() / MB);
            
            // 非堆内存信息
            MemoryUsage nonHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();
            memoryInfo.put("nonHeapInit", nonHeapMemoryUsage.getInit() / MB);
            memoryInfo.put("nonHeapUsed", nonHeapMemoryUsage.getUsed() / MB);
            memoryInfo.put("nonHeapCommitted", nonHeapMemoryUsage.getCommitted() / MB);
            memoryInfo.put("nonHeapMax", nonHeapMemoryUsage.getMax() / MB);
            
            // 计算使用率
            double heapUsageRate = (double) heapMemoryUsage.getUsed() / heapMemoryUsage.getMax() * 100;
            memoryInfo.put("heapUsageRate", heapUsageRate);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return memoryInfo;
    }

    @Override
    public Map<String, Object> getThreadInfo() {
        Map<String, Object> threadInfo = new HashMap<>();
        
        try {
            ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
            
            threadInfo.put("threadCount", threadMXBean.getThreadCount());
            threadInfo.put("peakThreadCount", threadMXBean.getPeakThreadCount());
            threadInfo.put("totalStartedThreadCount", threadMXBean.getTotalStartedThreadCount());
            threadInfo.put("daemonThreadCount", threadMXBean.getDaemonThreadCount());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return threadInfo;
    }
}
