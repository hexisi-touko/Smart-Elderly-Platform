<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="老人姓名" prop="elderlyName">
        <el-input
          v-model="queryParams.elderlyName"
          placeholder="请输入老人姓名"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="慢病类型" prop="chronicType">
        <el-input
          v-model="queryParams.chronicType"
          placeholder="请输入慢病类型"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['elderly:elderlyChronic:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['elderly:elderlyChronic:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['elderly:elderlyChronic:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['elderly:elderlyChronic:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="elderlyChronicList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="唯一标识" align="center" prop="id" />
      <el-table-column label="老人姓名" align="center" prop="elderlyName" />
      <el-table-column label="慢病类型" align="center" prop="chronicType" />
      <el-table-column label="确诊日期" align="center" prop="diagnosisDate" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.diagnosisDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['elderly:elderlyChronic:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['elderly:elderlyChronic:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改老人慢病关联管理对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="elderlyChronicRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="关联老人" prop="elderlyId">
          <el-select v-model="form.elderlyId" filterable remote reserve-keyword
            placeholder="请输入老人姓名搜索" :remote-method="remoteSearchElderly"
            :loading="elderlyLoading" style="width: 100%">
            <el-option v-for="item in elderlyOptions" :key="item.elderlyId"
              :label="item.name" :value="item.elderlyId" />
          </el-select>
        </el-form-item>
        <el-form-item label="慢病类型" prop="chronicType">
          <el-input v-model="form.chronicType" placeholder="请输入慢病类型" />
        </el-form-item>
        <el-form-item label="确诊日期" prop="diagnosisDate">
          <el-date-picker clearable
            v-model="form.diagnosisDate"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择确诊日期">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="ElderlyChronic">
import { listElderlyChronic, getElderlyChronic, delElderlyChronic, addElderlyChronic, updateElderlyChronic } from "@/api/elderly/elderlyChronic"
import { listElderly } from "@/api/elderly/elderly"

const { proxy } = getCurrentInstance()

const elderlyChronicList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
const elderlyOptions = ref([])
const elderlyLoading = ref(false)

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    elderlyId: null,
    elderlyName: null,
    chronicType: null,
  },
  rules: {
    elderlyId: [
      { required: true, message: "关联老人ID不能为空", trigger: "blur" }
    ],
    chronicType: [
      { required: true, message: "慢病类型不能为空", trigger: "blur" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询老人慢病关联管理列表 */
function getList() {
  loading.value = true
  listElderlyChronic(queryParams.value).then(response => {
    elderlyChronicList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

// 取消按钮
function cancel() {
  open.value = false
  reset()
}

// 表单重置
function reset() {
  form.value = {
    id: null,
    elderlyId: null,
    chronicType: null,
    diagnosisDate: null,
    isDeleted: null,
    createTime: null,
    updateTime: null
  }
  proxy.resetForm("elderlyChronicRef")
}

/** 远程搜索老人 */
function remoteSearchElderly(query) {
  if (query) {
    elderlyLoading.value = true
    listElderly({ name: query, pageNum: 1, pageSize: 20 }).then(res => {
      elderlyOptions.value = res.rows
      elderlyLoading.value = false
    })
  } else { elderlyOptions.value = [] }
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef")
  handleQuery()
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加老人慢病关联管理"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _id = row.id || ids.value
  getElderlyChronic(_id).then(response => {
    form.value = response.data
    if (form.value.elderlyId) {
      listElderly({ elderlyId: form.value.elderlyId, pageNum: 1, pageSize: 10 }).then(res => { elderlyOptions.value = res.rows })
    }
    open.value = true
    title.value = "修改老人慢病关联管理"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["elderlyChronicRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateElderlyChronic(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addElderlyChronic(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value
  proxy.$modal.confirm('是否确认删除老人慢病关联管理编号为"' + _ids + '"的数据项？').then(function() {
    return delElderlyChronic(_ids)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('elderly/elderlyChronic/export', {
    ...queryParams.value
  }, `elderlyChronic_${new Date().getTime()}.xlsx`)
}

getList()
</script>
