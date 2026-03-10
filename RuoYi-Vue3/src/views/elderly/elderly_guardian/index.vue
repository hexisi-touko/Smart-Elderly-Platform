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
      <el-form-item label="监护人姓名" prop="guardianName">
        <el-input
          v-model="queryParams.guardianName"
          placeholder="请输入监护人姓名"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="授权状态" prop="authorizationStatus">
        <el-select v-model="queryParams.authorizationStatus" placeholder="请选择授权状态" clearable>
          <el-option
            v-for="dict in authorization_status"
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
          v-hasPermi="['elderly:elderly_guardian:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['elderly:elderly_guardian:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['elderly:elderly_guardian:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['elderly:elderly_guardian:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="elderly_guardianList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="唯一标识" align="center" prop="id" />
      <el-table-column label="老人姓名" align="center" prop="elderlyName" />
      <el-table-column label="监护人姓名" align="center" prop="guardianName" />
      <el-table-column label="授权状态" align="center" prop="authorizationStatus">
        <template #default="scope">
          <dict-tag :options="authorization_status" :value="scope.row.authorizationStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['elderly:elderly_guardian:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['elderly:elderly_guardian:remove']">删除</el-button>
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

    <!-- 添加或修改老人-监护人关联对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="elderly_guardianRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="关联老人" prop="elderlyId">
          <el-select v-model="form.elderlyId" filterable remote reserve-keyword
            placeholder="请输入老人姓名搜索" :remote-method="remoteSearchElderly"
            :loading="elderlyLoading" style="width: 100%">
            <el-option v-for="item in elderlyOptions" :key="item.elderlyId"
              :label="item.name" :value="item.elderlyId" />
          </el-select>
        </el-form-item>
        <el-form-item label="关联监护人" prop="guardianId">
          <el-select v-model="form.guardianId" filterable remote reserve-keyword
            placeholder="请输入监护人姓名搜索" :remote-method="remoteSearchGuardian"
            :loading="guardianLoading" style="width: 100%">
            <el-option v-for="item in guardianOptions" :key="item.guardianId"
              :label="item.name" :value="item.guardianId" />
          </el-select>
        </el-form-item>
        <el-form-item label="授权状态" prop="authorizationStatus">
          <el-select v-model="form.authorizationStatus" placeholder="请选择授权状态">
            <el-option
              v-for="dict in authorization_status"
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

<script setup name="Elderly_guardian">
import { listElderly_guardian, getElderly_guardian, delElderly_guardian, addElderly_guardian, updateElderly_guardian } from "@/api/elderly/elderly_guardian"
import { listElderly } from "@/api/elderly/elderly"
import { listGuardian } from "@/api/elderly/guardian"

const { proxy } = getCurrentInstance()
const { authorization_status } = proxy.useDict('authorization_status')

const elderly_guardianList = ref([])
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
const guardianOptions = ref([])
const guardianLoading = ref(false)

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    elderlyId: null,
    elderlyName: null,
    guardianId: null,
    guardianName: null,
    authorizationStatus: null,
  },
  rules: {
    elderlyId: [
      { required: true, message: "关联老人ID不能为空", trigger: "blur" }
    ],
    guardianId: [
      { required: true, message: "关联监护人ID不能为空", trigger: "blur" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询老人-监护人关联列表 */
function getList() {
  loading.value = true
  listElderly_guardian(queryParams.value).then(response => {
    elderly_guardianList.value = response.rows
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
    guardianId: null,
    authorizationStatus: null,
    isDeleted: null,
    createTime: null,
    updateTime: null
  }
  proxy.resetForm("elderly_guardianRef")
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

/** 远程搜索监护人 */
function remoteSearchGuardian(query) {
  if (query) {
    guardianLoading.value = true
    listGuardian({ name: query, pageNum: 1, pageSize: 20 }).then(res => {
      guardianOptions.value = res.rows
      guardianLoading.value = false
    })
  } else { guardianOptions.value = [] }
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
  title.value = "添加老人-监护人关联"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _id = row.id || ids.value
  getElderly_guardian(_id).then(response => {
    form.value = response.data
    if (form.value.elderlyId) {
      listElderly({ elderlyId: form.value.elderlyId, pageNum: 1, pageSize: 10 }).then(res => { elderlyOptions.value = res.rows })
    }
    if (form.value.guardianId) {
      listGuardian({ guardianId: form.value.guardianId, pageNum: 1, pageSize: 10 }).then(res => { guardianOptions.value = res.rows })
    }
    open.value = true
    title.value = "修改老人-监护人关联"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["elderly_guardianRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateElderly_guardian(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addElderly_guardian(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除老人-监护人关联编号为"' + _ids + '"的数据项？').then(function() {
    return delElderly_guardian(_ids)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('elderly/elderly_guardian/export', {
    ...queryParams.value
  }, `elderly_guardian_${new Date().getTime()}.xlsx`)
}

getList()
</script>
