<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="服务商名称" prop="providerName">
        <el-input
          v-model="queryParams.providerName"
          placeholder="请输入服务商名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="结算周期" prop="settlementPeriod">
        <el-input
          v-model="queryParams.settlementPeriod"
          placeholder="请输入结算周期"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="订单数量" prop="orderCount">
        <el-input
          v-model="queryParams.orderCount"
          placeholder="请输入订单数量"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="结算状态" prop="settlementStatus">
        <el-select v-model="queryParams.settlementStatus" placeholder="请选择结算状态" clearable>
          <el-option
            v-for="dict in settlement_status"
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
          v-hasPermi="['order:settlement:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['order:settlement:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['order:settlement:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['order:settlement:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="settlementList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="结算id" align="center" prop="settlementId" />
      <el-table-column label="服务商名称" align="center" prop="providerName" />
      <el-table-column label="结算周期" align="center" prop="settlementPeriod" />
      <el-table-column label="订单数量" align="center" prop="orderCount" />
      <el-table-column label="结算总金额" align="center" prop="totalAmount" />
      <el-table-column label="平台服务费" align="center" prop="platformFee" />
      <el-table-column label="实际结算金额" align="center" prop="actualAmount" />
      <el-table-column label="结算状态" align="center" prop="settlementStatus">
        <template #default="scope">
          <dict-tag :options="settlement_status" :value="scope.row.settlementStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="提现申请时间" align="center" prop="applyTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.applyTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="审核时间" align="center" prop="approveTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.approveTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="转账时间" align="center" prop="transferTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.transferTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="转账凭证URL" align="center" prop="transferVoucher" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['order:settlement:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['order:settlement:remove']">删除</el-button>
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

    <!-- 添加或修改服务商结算管理对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="settlementRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="关联服务商" prop="providerId">
          <el-select v-model="form.providerId" filterable remote reserve-keyword
            placeholder="请输入服务商名称搜索" :remote-method="remoteSearchProvider"
            :loading="providerLoading" style="width: 100%">
            <el-option v-for="item in providerOptions" :key="item.providerId"
              :label="item.providerName" :value="item.providerId" />
          </el-select>
        </el-form-item>
        <el-form-item label="结算周期" prop="settlementPeriod">
          <el-input v-model="form.settlementPeriod" placeholder="请输入结算周期" />
        </el-form-item>
        <el-form-item label="订单数量" prop="orderCount">
          <el-input v-model="form.orderCount" placeholder="请输入订单数量" />
        </el-form-item>
        <el-form-item label="结算总金额" prop="totalAmount">
          <el-input v-model="form.totalAmount" placeholder="请输入结算总金额" />
        </el-form-item>
        <el-form-item label="平台服务费" prop="platformFee">
          <el-input v-model="form.platformFee" placeholder="请输入平台服务费" />
        </el-form-item>
        <el-form-item label="结算状态" prop="settlementStatus">
          <el-select v-model="form.settlementStatus" placeholder="请选择结算状态">
            <el-option
              v-for="dict in settlement_status"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="提现申请时间" prop="applyTime">
          <el-date-picker clearable
            v-model="form.applyTime"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择提现申请时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="审核时间" prop="approveTime">
          <el-date-picker clearable
            v-model="form.approveTime"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择审核时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="转账时间" prop="transferTime">
          <el-date-picker clearable
            v-model="form.transferTime"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择转账时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="转账凭证URL" prop="transferVoucher">
          <el-input v-model="form.transferVoucher" placeholder="请输入转账凭证URL" />
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

<script setup name="Settlement">
import { listSettlement, getSettlement, delSettlement, addSettlement, updateSettlement } from "@/api/order/settlement"
import { listServiceProvider } from "@/api/service/serviceProvider"

const { proxy } = getCurrentInstance()
const { settlement_status } = proxy.useDict('settlement_status')

const settlementList = ref([])
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
    providerId: null,
    providerName: null,
    settlementPeriod: null,
    orderCount: null,
    settlementStatus: null,
  },
  rules: {
    providerId: [
      { required: true, message: "关联服务商ID不能为空", trigger: "blur" }
    ],
    settlementPeriod: [
      { required: true, message: "结算周期不能为空", trigger: "blur" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询服务商结算管理列表 */
function getList() {
  loading.value = true
  listSettlement(queryParams.value).then(response => {
    settlementList.value = response.rows
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
    settlementId: null,
    providerId: null,
    settlementPeriod: null,
    orderCount: null,
    totalAmount: null,
    platformFee: null,
    actualAmount: null,
    settlementStatus: null,
    applyTime: null,
    approveTime: null,
    transferTime: null,
    transferVoucher: null,
    isDeleted: null,
    createTime: null,
    updateTime: null
  }
  proxy.resetForm("settlementRef")
}

/** 远程搜索服务商 */
function remoteSearchProvider(query) {
  if (query) {
    providerLoading.value = true
    listServiceProvider({ providerName: query, pageNum: 1, pageSize: 20 }).then(res => {
      providerOptions.value = res.rows
      providerLoading.value = false
    })
  } else { providerOptions.value = [] }
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
  ids.value = selection.map(item => item.settlementId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加服务商结算管理"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _settlementId = row.settlementId || ids.value
  getSettlement(_settlementId).then(response => {
    form.value = response.data
    if (form.value.providerId) {
      listServiceProvider({ providerId: form.value.providerId, pageNum: 1, pageSize: 10 }).then(res => {
        providerOptions.value = res.rows
      })
    }
    open.value = true
    title.value = "修改服务商结算管理"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["settlementRef"].validate(valid => {
    if (valid) {
      if (form.value.settlementId != null) {
        updateSettlement(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addSettlement(form.value).then(response => {
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
  const _settlementIds = row.settlementId || ids.value
  proxy.$modal.confirm('是否确认删除服务商结算管理编号为"' + _settlementIds + '"的数据项？').then(function() {
    return delSettlement(_settlementIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('order/settlement/export', {
    ...queryParams.value
  }, `settlement_${new Date().getTime()}.xlsx`)
}

getList()
</script>
