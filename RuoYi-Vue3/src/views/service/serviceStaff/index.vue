<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="服务人员姓名" prop="staffName">
        <el-input
          v-model="queryParams.staffName"
          placeholder="请输入服务人员姓名"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="服务商名称" prop="providerName">
        <el-input
          v-model="queryParams.providerName"
          placeholder="请输入服务商名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="服务人员手机号" prop="phone">
        <el-input
          v-model="queryParams.phone"
          placeholder="请输入服务人员手机号"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="人员类型" prop="staffType">
        <el-select v-model="queryParams.staffType" placeholder="请选择人员类型" clearable>
          <el-option
            v-for="dict in staff_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="工作状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择工作状态" clearable>
          <el-option
            v-for="dict in staff_status"
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
          v-hasPermi="['service:serviceStaff:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['service:serviceStaff:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['service:serviceStaff:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['service:serviceStaff:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="serviceStaffList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="服务人员id" align="center" prop="staffId" />
      <el-table-column label="服务人员姓名" align="center" prop="staffName" />
      <el-table-column label="服务商名称" align="center" prop="providerName" />
      <el-table-column label="服务人员手机号" align="center" prop="phone" />
      <el-table-column label="职业资格证书编号" align="center" prop="certificate" />
      <el-table-column label="人员类型" align="center" prop="staffType">
        <template #default="scope">
          <dict-tag :options="staff_type" :value="scope.row.staffType"/>
        </template>
      </el-table-column>
      <el-table-column label="工作状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :options="staff_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['service:serviceStaff:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['service:serviceStaff:remove']">删除</el-button>
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

    <!-- 添加或修改服务人员管理对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="serviceStaffRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="服务人员姓名" prop="staffName">
          <el-input v-model="form.staffName" placeholder="请输入服务人员姓名" />
        </el-form-item>
        <el-form-item label="关联服务商" prop="providerId">
          <el-select
            v-model="form.providerId"
            filterable
            remote
            reserve-keyword
            placeholder="请输入服务商名称搜索"
            :remote-method="remoteSearchProvider"
            :loading="providerLoading"
            style="width: 100%"
          >
            <el-option
              v-for="item in providerOptions"
              :key="item.providerId"
              :label="item.providerName"
              :value="item.providerId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="服务人员手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入服务人员手机号" />
        </el-form-item>
        <el-form-item label="身份证号" prop="idCard">
          <el-input v-model="form.idCard" placeholder="请输入身份证号" />
        </el-form-item>
        <el-form-item label="职业资格证书编号" prop="certificate">
          <el-input v-model="form.certificate" placeholder="请输入职业资格证书编号" />
        </el-form-item>
        <el-form-item label="人员类型" prop="staffType">
          <el-select v-model="form.staffType" placeholder="请选择人员类型">
            <el-option
              v-for="dict in staff_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="工作状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择工作状态">
            <el-option
              v-for="dict in staff_status"
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

<script setup name="ServiceStaff">
import { listServiceStaff, getServiceStaff, delServiceStaff, addServiceStaff, updateServiceStaff } from "@/api/service/serviceStaff"
import { listServiceProvider } from "@/api/service/serviceProvider"

const { proxy } = getCurrentInstance()
const { staff_status, staff_type } = proxy.useDict('staff_status', 'staff_type')

const serviceStaffList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
const providerOptions = ref([])
const providerLoading = ref(false)

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    staffName: null,
    providerId: null,
    providerName: null,
    phone: null,
    staffType: null,
    status: null,
  },
  rules: {
    staffName: [
      { required: true, message: "服务人员姓名不能为空", trigger: "blur" }
    ],
    providerId: [
      { required: true, message: "关联所属服务商ID不能为空", trigger: "blur" }
    ],
    phone: [
      { required: true, message: "服务人员手机号不能为空", trigger: "blur" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询服务人员管理列表 */
function getList() {
  loading.value = true
  listServiceStaff(queryParams.value).then(response => {
    serviceStaffList.value = response.rows
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
    staffId: null,
    staffName: null,
    providerId: null,
    phone: null,
    idCard: null,
    certificate: null,
    staffType: null,
    status: null,
    isDeleted: null,
    createTime: null,
    updateTime: null
  }
  proxy.resetForm("serviceStaffRef")
}

/** 远程搜索服务商 */
function remoteSearchProvider(query) {
  if (query) {
    providerLoading.value = true
    listServiceProvider({ providerName: query, pageNum: 1, pageSize: 20 }).then(res => {
      providerOptions.value = res.rows
      providerLoading.value = false
    })
  } else {
    providerOptions.value = []
  }
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
  ids.value = selection.map(item => item.staffId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加服务人员管理"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _staffId = row.staffId || ids.value
  getServiceStaff(_staffId).then(response => {
    form.value = response.data
    if (form.value.providerId) {
      listServiceProvider({ providerId: form.value.providerId, pageNum: 1, pageSize: 10 }).then(res => {
        providerOptions.value = res.rows
      })
    }
    open.value = true
    title.value = "修改服务人员管理"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["serviceStaffRef"].validate(valid => {
    if (valid) {
      if (form.value.staffId != null) {
        updateServiceStaff(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addServiceStaff(form.value).then(response => {
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
  const _staffIds = row.staffId || ids.value
  proxy.$modal.confirm('是否确认删除服务人员管理编号为"' + _staffIds + '"的数据项？').then(function() {
    return delServiceStaff(_staffIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('service/serviceStaff/export', {
    ...queryParams.value
  }, `serviceStaff_${new Date().getTime()}.xlsx`)
}

getList()
</script>
