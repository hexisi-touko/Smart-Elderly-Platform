<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="关联C端用户ID" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入关联C端用户ID"
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
      <el-form-item label="联系人手机号" prop="contactPhone">
        <el-input
          v-model="queryParams.contactPhone"
          placeholder="请输入联系人手机号"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="审核状态" prop="auditStatus">
        <el-select v-model="queryParams.auditStatus" placeholder="请选择审核状态" clearable>
          <el-option
            v-for="dict in provider_audit_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="服务范围" prop="serviceScope">
        <el-input
          v-model="queryParams.serviceScope"
          placeholder="请输入服务范围"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建时间" style="width: 308px">
        <el-date-picker
          v-model="daterangeCreateTime"
          value-format="YYYY-MM-DD"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
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
          v-hasPermi="['service:serviceProvider:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['service:serviceProvider:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['service:serviceProvider:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['service:serviceProvider:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="serviceProviderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="服务商id" align="center" prop="providerId" />
      <el-table-column label="关联C端用户ID" align="center" prop="userId" />
      <el-table-column label="服务商名称" align="center" prop="providerName" />
      <el-table-column label="资质许可证号" align="center" prop="licenseCode" />
      <el-table-column label="联系人姓名" align="center" prop="contactPerson" />
      <el-table-column label="联系人手机号" align="center" prop="contactPhone" />
      <el-table-column label="审核状态" align="center" prop="auditStatus">
        <template #default="scope">
          <dict-tag :options="provider_audit_status" :value="scope.row.auditStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="服务范围" align="center" prop="serviceScope" />
      <el-table-column label="服务商综合评分" align="center" prop="score" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['service:serviceProvider:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['service:serviceProvider:remove']">删除</el-button>
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

    <!-- 添加或修改服务商管理对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="serviceProviderRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="关联C端用户ID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入关联C端用户ID" />
        </el-form-item>
        <el-form-item label="服务商名称" prop="providerName">
          <el-input v-model="form.providerName" placeholder="请输入服务商名称" />
        </el-form-item>
        <el-form-item label="资质许可证号" prop="licenseCode">
          <el-input v-model="form.licenseCode" placeholder="请输入资质许可证号" />
        </el-form-item>
        <el-form-item label="联系人姓名" prop="contactPerson">
          <el-input v-model="form.contactPerson" placeholder="请输入联系人姓名" />
        </el-form-item>
        <el-form-item label="联系人手机号" prop="contactPhone">
          <el-input v-model="form.contactPhone" placeholder="请输入联系人手机号" />
        </el-form-item>
        <el-form-item label="服务商办公地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入服务商办公地址" />
        </el-form-item>
        <el-form-item label="营业执照编号" prop="businessLicense">
          <el-input v-model="form.businessLicense" placeholder="请输入营业执照编号" />
        </el-form-item>
        <el-form-item label="审核状态" prop="auditStatus">
          <el-select v-model="form.auditStatus" placeholder="请选择审核状态">
            <el-option
              v-for="dict in provider_audit_status"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="服务范围" prop="serviceScope">
          <el-input v-model="form.serviceScope" placeholder="请输入服务范围" />
        </el-form-item>
        <el-form-item label="结算银行账号" prop="bankAccount">
          <el-input v-model="form.bankAccount" placeholder="请输入结算银行账号" />
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

<script setup name="ServiceProvider">
import { listServiceProvider, getServiceProvider, delServiceProvider, addServiceProvider, updateServiceProvider } from "@/api/service/serviceProvider"

const { proxy } = getCurrentInstance()
const { provider_audit_status } = proxy.useDict('provider_audit_status')

const serviceProviderList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
const daterangeCreateTime = ref([])

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    userId: null,
    providerName: null,
    contactPhone: null,
    auditStatus: null,
    serviceScope: null,
    createTime: null,
  },
  rules: {
    userId: [
      { required: true, message: "关联C端用户ID不能为空", trigger: "blur" }
    ],
    providerName: [
      { required: true, message: "服务商名称不能为空", trigger: "blur" }
    ],
    licenseCode: [
      { required: true, message: "资质许可证号不能为空", trigger: "blur" }
    ],
    contactPerson: [
      { required: true, message: "联系人姓名不能为空", trigger: "blur" }
    ],
    contactPhone: [
      { required: true, message: "联系人手机号不能为空", trigger: "blur" }
    ],
    address: [
      { required: true, message: "服务商办公地址不能为空", trigger: "blur" }
    ],
    serviceScope: [
      { required: true, message: "服务范围不能为空", trigger: "blur" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询服务商管理列表 */
function getList() {
  loading.value = true
  queryParams.value.params = {}
  if (null != daterangeCreateTime.value && '' != daterangeCreateTime.value) {
    queryParams.value.params["beginCreateTime"] = daterangeCreateTime.value[0]
    queryParams.value.params["endCreateTime"] = daterangeCreateTime.value[1]
  }
  listServiceProvider(queryParams.value).then(response => {
    serviceProviderList.value = response.rows
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
    providerId: null,
    userId: null,
    providerName: null,
    licenseCode: null,
    contactPerson: null,
    contactPhone: null,
    address: null,
    businessLicense: null,
    auditStatus: null,
    serviceScope: null,
    score: null,
    bankAccount: null,
    isDeleted: null,
    createTime: null,
    updateTime: null
  }
  proxy.resetForm("serviceProviderRef")
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  daterangeCreateTime.value = []
  proxy.resetForm("queryRef")
  handleQuery()
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.providerId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加服务商管理"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _providerId = row.providerId || ids.value
  getServiceProvider(_providerId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改服务商管理"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["serviceProviderRef"].validate(valid => {
    if (valid) {
      if (form.value.providerId != null) {
        updateServiceProvider(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addServiceProvider(form.value).then(response => {
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
  const _providerIds = row.providerId || ids.value
  proxy.$modal.confirm('是否确认删除服务商管理编号为"' + _providerIds + '"的数据项？').then(function() {
    return delServiceProvider(_providerIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('service/serviceProvider/export', {
    ...queryParams.value
  }, `serviceProvider_${new Date().getTime()}.xlsx`)
}

getList()
</script>
