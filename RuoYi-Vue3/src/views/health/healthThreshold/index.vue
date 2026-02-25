<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="关联老人ID" prop="elderlyId">
        <el-input
          v-model="queryParams.elderlyId"
          placeholder="请输入关联老人ID"
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
          v-hasPermi="['health:healthThreshold:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['health:healthThreshold:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['health:healthThreshold:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['health:healthThreshold:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="healthThresholdList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="阈值id" align="center" prop="thresholdId" />
      <el-table-column label="关联老人ID" align="center" prop="elderlyId" />
      <el-table-column label="收缩压上限阈值" align="center" prop="sysBpMax" />
      <el-table-column label="收缩压下限阈值" align="center" prop="sysBpMin" />
      <el-table-column label="舒张压上限阈值" align="center" prop="diaBpMax" />
      <el-table-column label="舒张压下限阈值" align="center" prop="diaBpMin" />
      <el-table-column label="心率上限阈值" align="center" prop="heartRateMax" />
      <el-table-column label="心率下限阈值" align="center" prop="heartRateMin" />
      <el-table-column label="血糖上限阈值" align="center" prop="bloodSugarMax" />
      <el-table-column label="血糖下限阈值" align="center" prop="bloodSugarMin" />
      <el-table-column label="体温上限阈值" align="center" prop="temperatureMax" />
      <el-table-column label="体温下限阈值" align="center" prop="temperatureMin" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['health:healthThreshold:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['health:healthThreshold:remove']">删除</el-button>
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

    <!-- 添加或修改健康阈值管理对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="healthThresholdRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="关联老人ID" prop="elderlyId">
          <el-input v-model="form.elderlyId" placeholder="请输入关联老人ID" />
        </el-form-item>
        <el-form-item label="收缩压上限阈值" prop="sysBpMax">
          <el-input v-model="form.sysBpMax" placeholder="请输入收缩压上限阈值" />
        </el-form-item>
        <el-form-item label="收缩压下限阈值" prop="sysBpMin">
          <el-input v-model="form.sysBpMin" placeholder="请输入收缩压下限阈值" />
        </el-form-item>
        <el-form-item label="舒张压上限阈值" prop="diaBpMax">
          <el-input v-model="form.diaBpMax" placeholder="请输入舒张压上限阈值" />
        </el-form-item>
        <el-form-item label="舒张压下限阈值" prop="diaBpMin">
          <el-input v-model="form.diaBpMin" placeholder="请输入舒张压下限阈值" />
        </el-form-item>
        <el-form-item label="心率上限阈值" prop="heartRateMax">
          <el-input v-model="form.heartRateMax" placeholder="请输入心率上限阈值" />
        </el-form-item>
        <el-form-item label="心率下限阈值" prop="heartRateMin">
          <el-input v-model="form.heartRateMin" placeholder="请输入心率下限阈值" />
        </el-form-item>
        <el-form-item label="血糖上限阈值" prop="bloodSugarMax">
          <el-input v-model="form.bloodSugarMax" placeholder="请输入血糖上限阈值" />
        </el-form-item>
        <el-form-item label="血糖下限阈值" prop="bloodSugarMin">
          <el-input v-model="form.bloodSugarMin" placeholder="请输入血糖下限阈值" />
        </el-form-item>
        <el-form-item label="体温上限阈值" prop="temperatureMax">
          <el-input v-model="form.temperatureMax" placeholder="请输入体温上限阈值" />
        </el-form-item>
        <el-form-item label="体温下限阈值" prop="temperatureMin">
          <el-input v-model="form.temperatureMin" placeholder="请输入体温下限阈值" />
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

<script setup name="HealthThreshold">
import { listHealthThreshold, getHealthThreshold, delHealthThreshold, addHealthThreshold, updateHealthThreshold } from "@/api/health/healthThreshold"

const { proxy } = getCurrentInstance()

const healthThresholdList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    elderlyId: null,
  },
  rules: {
    elderlyId: [
      { required: true, message: "关联老人ID不能为空", trigger: "blur" }
    ],
    sysBpMax: [
      { required: true, message: "收缩压上限阈值不能为空", trigger: "blur" }
    ],
    sysBpMin: [
      { required: true, message: "收缩压下限阈值不能为空", trigger: "blur" }
    ],
    diaBpMax: [
      { required: true, message: "舒张压上限阈值不能为空", trigger: "blur" }
    ],
    diaBpMin: [
      { required: true, message: "舒张压下限阈值不能为空", trigger: "blur" }
    ],
    heartRateMax: [
      { required: true, message: "心率上限阈值不能为空", trigger: "blur" }
    ],
    heartRateMin: [
      { required: true, message: "心率下限阈值不能为空", trigger: "blur" }
    ],
    bloodSugarMax: [
      { required: true, message: "血糖上限阈值不能为空", trigger: "blur" }
    ],
    bloodSugarMin: [
      { required: true, message: "血糖下限阈值不能为空", trigger: "blur" }
    ],
    temperatureMax: [
      { required: true, message: "体温上限阈值不能为空", trigger: "blur" }
    ],
    temperatureMin: [
      { required: true, message: "体温下限阈值不能为空", trigger: "blur" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询健康阈值管理列表 */
function getList() {
  loading.value = true
  listHealthThreshold(queryParams.value).then(response => {
    healthThresholdList.value = response.rows
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
    thresholdId: null,
    elderlyId: null,
    sysBpMax: null,
    sysBpMin: null,
    diaBpMax: null,
    diaBpMin: null,
    heartRateMax: null,
    heartRateMin: null,
    bloodSugarMax: null,
    bloodSugarMin: null,
    temperatureMax: null,
    temperatureMin: null,
    isDeleted: null,
    createTime: null,
    updateTime: null
  }
  proxy.resetForm("healthThresholdRef")
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
  ids.value = selection.map(item => item.thresholdId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加健康阈值管理"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _thresholdId = row.thresholdId || ids.value
  getHealthThreshold(_thresholdId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改健康阈值管理"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["healthThresholdRef"].validate(valid => {
    if (valid) {
      if (form.value.thresholdId != null) {
        updateHealthThreshold(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addHealthThreshold(form.value).then(response => {
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
  const _thresholdIds = row.thresholdId || ids.value
  proxy.$modal.confirm('是否确认删除健康阈值管理编号为"' + _thresholdIds + '"的数据项？').then(function() {
    return delHealthThreshold(_thresholdIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('health/healthThreshold/export', {
    ...queryParams.value
  }, `healthThreshold_${new Date().getTime()}.xlsx`)
}

getList()
</script>
