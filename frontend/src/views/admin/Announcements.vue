<template>
  <div class="admin-container">
    <h2 class="page-title">系统公告管理</h2>
    
    <!-- 发布公告 -->
    <el-card class="admin-card">
      <template #header>
        <div class="card-header">
          <span class="card-title">发布公告</span>
        </div>
      </template>
      <el-form :model="announcementForm" :rules="rules" ref="announcementForm">
        <el-form-item label="公告标题" prop="title">
          <el-input v-model="announcementForm.title" placeholder="请输入公告标题"></el-input>
        </el-form-item>
        <el-form-item label="公告内容" prop="content">
          <el-input v-model="announcementForm.content" type="textarea" placeholder="请输入公告内容" :rows="4"></el-input>
        </el-form-item>
        <el-form-item label="优先级" prop="priority">
          <el-select v-model="announcementForm.priority" placeholder="请选择优先级">
            <el-option label="低" value="low"></el-option>
            <el-option label="中" value="medium"></el-option>
            <el-option label="高" value="high"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="有效期" prop="expiryDate">
          <el-date-picker v-model="announcementForm.expiryDate" type="date" placeholder="选择有效期" format="yyyy-MM-dd" value-format="yyyy-MM-dd"></el-date-picker>
        </el-form-item>
        <el-form-item label="发布范围" prop="target">
          <el-select v-model="announcementForm.target" placeholder="请选择发布范围">
            <el-option label="所有用户" value="all"></el-option>
            <el-option label="学生" value="student"></el-option>
            <el-option label="教师" value="teacher"></el-option>
            <el-option label="管理员" value="admin"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="publishAnnouncement">发布公告</el-button>
        </el-form-item>
      </el-form>
    </el-card>

