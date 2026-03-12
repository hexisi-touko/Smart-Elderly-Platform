<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="监护人姓名" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入监护人姓名"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="监护人手机号" prop="phone">
        <el-input
          v-model="queryParams.phone"
          placeholder="请输入监护人手机号"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="与老人关系" prop="relationship">
        <el-select v-model="queryParams.relationship" placeholder="请选择与老人关系" clearable>
          <el-option
            v-for="dict in guardian_relationship"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="是否主要监护人" prop="isPrimary">
        <el-select v-model="queryParams.isPrimary" placeholder="请选择是否主要监护人" clearable>
          <el-option
            v-for="dict in sys_yes_no"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
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
          v-hasPermi="['elderly:guardian:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['elderly:guardian:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['elderly:guardian:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['elderly:guardian:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="guardianList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="监护人姓名" align="center" prop="name" />
      <el-table-column label="监护人手机号" align="center" prop="phone" />
      <el-table-column label="与老人关系" align="center" prop="relationship">
        <template #default="scope">
          <dict-tag :options="guardian_relationship" :value="scope.row.relationship"/>
        </template>
      </el-table-column>
      <el-table-column label="是否主要监护人" align="center" prop="isPrimary">
        <template #default="scope">
          <dict-tag :options="sys_yes_no" :value="scope.row.isPrimary"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['elderly:guardian:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['elderly:guardian:remove']">删除</el-button>
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

    <!-- 添加或修改监护人信息对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="guardianRef" :model="form" :rules="rules" label-width="80px">

        <el-form-item label="监护人姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入监护人姓名" />
        </el-form-item>
        <el-form-item label="身份证号" prop="idCard">
          <el-input v-model="form.idCard" placeholder="请输入身份证号" />
        </el-form-item>
        <el-form-item label="监护人手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入监护人手机号" />
        </el-form-item>
        <el-form-item label="与老人关系" prop="relationship">
          <el-select v-model="form.relationship" placeholder="请选择与老人关系">
            <el-option
              v-for="dict in guardian_relationship"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否主要监护人" prop="isPrimary">
          <el-select v-model="form.isPrimary" placeholder="请选择是否主要监护人">
            <el-option
              v-for="dict in sys_yes_no"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
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

<script setup name="Guardian">
import { listGuardian, getGuardian, delGuardian, addGuardian, updateGuardian } from "@/api/elderly/guardian"

const { proxy } = getCurrentInstance()
const { sys_yes_no, guardian_relationship } = proxy.useDict('sys_yes_no', 'guardian_relationship')

const guardianList = ref([])
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
    userId: null,
    name: null,
    phone: null,
    relationship: null,
    isPrimary: null,
  },
  rules: {

    name: [
      { required: true, message: "监护人姓名不能为空", trigger: "blur" }
    ],
    idCard: [
      { required: true, message: "身份证号不能为空", trigger: "blur" }
    ],
    phone: [
      { required: true, message: "监护人手机号不能为空", trigger: "blur" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询监护人信息列表 */
function getList() {
  loading.value = true
  listGuardian(queryParams.value).then(response => {
    guardianList.value = response.rows
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
    guardianId: null,
    userId: null,
    name: null,
    idCard: null,
    phone: null,
    relationship: null,
    isPrimary: null,
    isDeleted: null,
    createTime: null,
    updateTime: null
  }
  proxy.resetForm("guardianRef")
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
  ids.value = selection.map(item => item.guardianId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加监护人信息"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _guardianId = row.guardianId || ids.value
  getGuardian(_guardianId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改监护人信息"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["guardianRef"].validate(valid => {
    if (valid) {
      if (form.value.guardianId != null) {
        updateGuardian(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addGuardian(form.value).then(response => {
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
  const _guardianIds = row.guardianId || ids.value
  proxy.$modal.confirm('是否确认删除监护人信息编号为"' + _guardianIds + '"的数据项？').then(function() {
    return delGuardian(_guardianIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('elderly/guardian/export', {
    ...queryParams.value
  }, `guardian_${new Date().getTime()}.xlsx`)
}

getList()
</script>
