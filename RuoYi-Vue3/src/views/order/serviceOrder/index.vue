<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="老人姓名" prop="elderlyName">
        <el-input v-model="queryParams.elderlyName" placeholder="请输入老人姓名" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="订单编号" prop="orderNo">
        <el-input v-model="queryParams.orderNo" placeholder="请输入订单编号" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="订单状态" prop="orderStatus">
        <el-select v-model="queryParams.orderStatus" placeholder="全部状态" clearable>
          <el-option v-for="dict in order_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="服务时间" prop="serviceTime">
        <el-date-picker clearable v-model="queryParams.serviceTime" type="date" value-format="YYYY-MM-DD" placeholder="请选择约定服务时间" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['order:serviceOrder:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate" v-hasPermi="['order:serviceOrder:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['order:serviceOrder:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['order:serviceOrder:export']">导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="serviceOrderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="订单编号" align="center" prop="orderNo" width="150" />
      <el-table-column label="老人姓名" align="center" prop="elderlyName" width="100" />
      <el-table-column label="服务项目" align="center" prop="itemName" width="120" />
      <el-table-column label="服务商" align="center" prop="providerName" width="120" />
      <el-table-column label="服务人员" align="center" prop="staffName" width="100">
        <template #default="scope">
          <span>{{ scope.row.staffName || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="订单金额" align="center" prop="orderAmount" width="100" />
      <el-table-column label="服务时间" align="center" prop="serviceTime" width="120">
        <template #default="scope">
          <span>{{ parseTime(scope.row.serviceTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="订单状态" align="center" prop="orderStatus" width="100">
        <template #default="scope">
          <dict-tag :options="order_status" :value="scope.row.orderStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="260">
        <template #default="scope">
          <!-- 指派员工：仅待接单(0)状态 -->
          <el-button link type="primary" icon="UserFilled" @click="handleAssign(scope.row)"
            v-if="scope.row.orderStatus == 0" v-hasPermi="['order:serviceOrder:edit']">指派</el-button>
          <!-- 开始服务：仅已接单(1)状态 -->
          <el-button link type="success" icon="VideoPlay" @click="handleStartService(scope.row)"
            v-if="scope.row.orderStatus == 1" v-hasPermi="['order:serviceOrder:edit']">开始服务</el-button>
          <!-- 标记完成：仅服务中(2)状态 -->
          <el-button link type="warning" icon="CircleCheck" @click="handleComplete(scope.row)"
            v-if="scope.row.orderStatus == 2" v-hasPermi="['order:serviceOrder:edit']">标记完成</el-button>
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['order:serviceOrder:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['order:serviceOrder:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination v-show="total>0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改服务订单对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="serviceOrderRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="关联老人" prop="elderlyId">
          <el-select v-model="form.elderlyId" filterable remote reserve-keyword
            placeholder="请输入老人姓名搜索" :remote-method="remoteSearchElderly"
            :loading="elderlyLoading" style="width: 100%">
            <el-option v-for="item in elderlyOptions" :key="item.elderlyId"
              :label="item.name" :value="item.elderlyId" />
          </el-select>
        </el-form-item>
        <el-form-item label="关联服务商" prop="providerId">
          <el-select v-model="form.providerId" filterable remote reserve-keyword
            placeholder="请输入服务商名称搜索" :remote-method="remoteSearchProvider"
            :loading="providerLoading" style="width: 100%">
            <el-option v-for="item in providerOptions" :key="item.providerId"
              :label="item.providerName" :value="item.providerId" />
          </el-select>
        </el-form-item>
        <el-form-item label="服务项目" prop="serviceItemId">
          <el-select v-model="form.serviceItemId" filterable remote reserve-keyword
            placeholder="请输入服务项目名称搜索" :remote-method="remoteSearchItem"
            :loading="itemLoading" style="width: 100%">
            <el-option v-for="item in itemOptions" :key="item.itemId"
              :label="item.itemName" :value="item.itemId" />
          </el-select>
        </el-form-item>
        <el-form-item label="订单编号" prop="orderNo">
          <el-input v-model="form.orderNo" placeholder="请输入订单编号" />
        </el-form-item>
        <el-form-item label="订单金额" prop="orderAmount">
          <el-input v-model="form.orderAmount" placeholder="请输入订单金额" />
        </el-form-item>
        <el-form-item label="服务时间" prop="serviceTime">
          <el-date-picker clearable v-model="form.serviceTime" type="date" value-format="YYYY-MM-DD" placeholder="请选择约定服务时间" />
        </el-form-item>
        <el-form-item label="服务要求" prop="serviceRequirements">
          <el-input v-model="form.serviceRequirements" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="订单状态" prop="orderStatus">
          <el-select v-model="form.orderStatus" placeholder="请选择订单状态">
            <el-option v-for="dict in order_status" :key="dict.value" :label="dict.label" :value="parseInt(dict.value)" />
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

    <!-- 指派员工对话框 -->
    <el-dialog title="指派服务人员" v-model="assignOpen" width="450px" append-to-body>
      <el-form label-width="100px">
        <el-form-item label="订单编号">
          <el-input :value="assignOrderNo" disabled />
        </el-form-item>
        <el-form-item label="服务人员" required>
          <el-select v-model="assignStaffId" filterable remote reserve-keyword
            placeholder="请输入员工姓名搜索" :remote-method="remoteSearchStaff"
            :loading="staffLoading" style="width: 100%">
            <el-option v-for="item in staffOptions" :key="item.staffId"
              :label="`${item.staffName} (${item.phone || '-'})`" :value="item.staffId" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitAssign" :loading="assignLoading">确 定</el-button>
          <el-button @click="assignOpen = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="ServiceOrder">
import { listServiceOrder, getServiceOrder, delServiceOrder, addServiceOrder, updateServiceOrder, assignStaff, changeOrderStatus } from "@/api/order/serviceOrder"
import { listElderly } from "@/api/elderly/elderly"
import { listServiceProvider } from "@/api/service/serviceProvider"
import { listServiceItem } from "@/api/service/serviceItem"
import { listServiceStaff } from "@/api/service/serviceStaff"

const { proxy } = getCurrentInstance()
const { order_status } = proxy.useDict('order_status')

const serviceOrderList = ref([])
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
const providerOptions = ref([])
const providerLoading = ref(false)
const itemOptions = ref([])
const itemLoading = ref(false)

// 指派相关
const assignOpen = ref(false)
const assignOrderId = ref(null)
const assignOrderNo = ref('')
const assignStaffId = ref(null)
const staffOptions = ref([])
const staffLoading = ref(false)
const assignLoading = ref(false)

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    elderlyId: null,
    elderlyName: null,
    providerId: null,
    serviceItemId: null,
    orderNo: null,
    orderStatus: null,
    serviceTime: null,
    serviceAddress: null,
  },
  rules: {
    elderlyId: [{ required: true, message: "关联老人ID不能为空", trigger: "blur" }],
    providerId: [{ required: true, message: "关联服务商ID不能为空", trigger: "blur" }],
    serviceItemId: [{ required: true, message: "关联服务项目ID不能为空", trigger: "blur" }],
    orderNo: [{ required: true, message: "订单编号不能为空", trigger: "blur" }],
    orderAmount: [{ required: true, message: "订单金额不能为空", trigger: "blur" }],
    serviceTime: [{ required: true, message: "约定服务时间不能为空", trigger: "blur" }],
    orderStatus: [{ required: true, message: "订单状态不能为空", trigger: "change" }],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询服务订单列表 */
function getList() {
  loading.value = true
  listServiceOrder(queryParams.value).then(response => {
    serviceOrderList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

function cancel() { open.value = false; reset() }

function reset() {
  form.value = {
    orderId: null, elderlyId: null, providerId: null, serviceItemId: null,
    orderNo: null, orderAmount: null, serviceTime: null, serviceAddress: null,
    serviceRequirements: null, orderStatus: null, acceptTime: null,
    startTime: null, completeTime: null, cancelReason: null,
    isDeleted: null, createTime: null, updateTime: null
  }
  proxy.resetForm("serviceOrderRef")
}

/** 远程搜索 */
function remoteSearchElderly(query) {
  if (query) {
    elderlyLoading.value = true
    listElderly({ name: query, pageNum: 1, pageSize: 20 }).then(res => { elderlyOptions.value = res.rows; elderlyLoading.value = false })
  } else { elderlyOptions.value = [] }
}
function remoteSearchProvider(query) {
  if (query) {
    providerLoading.value = true
    listServiceProvider({ providerName: query, pageNum: 1, pageSize: 20 }).then(res => { providerOptions.value = res.rows; providerLoading.value = false })
  } else { providerOptions.value = [] }
}
function remoteSearchItem(query) {
  if (query) {
    itemLoading.value = true
    listServiceItem({ itemName: query, pageNum: 1, pageSize: 20 }).then(res => { itemOptions.value = res.rows; itemLoading.value = false })
  } else { itemOptions.value = [] }
}
function remoteSearchStaff(query) {
  if (query) {
    staffLoading.value = true
    listServiceStaff({ staffName: query, pageNum: 1, pageSize: 20 }).then(res => { staffOptions.value = res.rows; staffLoading.value = false })
  } else { staffOptions.value = [] }
}

function handleQuery() { queryParams.value.pageNum = 1; getList() }
function resetQuery() { proxy.resetForm("queryRef"); handleQuery() }
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.orderId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

function handleAdd() { reset(); open.value = true; title.value = "添加服务订单" }

function handleUpdate(row) {
  reset()
  const _orderId = row.orderId || ids.value
  getServiceOrder(_orderId).then(response => {
    form.value = response.data
    if (form.value.elderlyId) { listElderly({ elderlyId: form.value.elderlyId, pageNum: 1, pageSize: 10 }).then(res => { elderlyOptions.value = res.rows }) }
    if (form.value.providerId) { listServiceProvider({ providerId: form.value.providerId, pageNum: 1, pageSize: 10 }).then(res => { providerOptions.value = res.rows }) }
    if (form.value.serviceItemId) { listServiceItem({ itemId: form.value.serviceItemId, pageNum: 1, pageSize: 10 }).then(res => { itemOptions.value = res.rows }) }
    open.value = true
    title.value = "修改服务订单"
  })
}

function submitForm() {
  proxy.$refs["serviceOrderRef"].validate(valid => {
    if (valid) {
      if (form.value.orderId != null) {
        updateServiceOrder(form.value).then(() => { proxy.$modal.msgSuccess("修改成功"); open.value = false; getList() })
      } else {
        addServiceOrder(form.value).then(() => { proxy.$modal.msgSuccess("新增成功"); open.value = false; getList() })
      }
    }
  })
}

function handleDelete(row) {
  const _orderIds = row.orderId || ids.value
  proxy.$modal.confirm('是否确认删除服务订单编号为"' + _orderIds + '"的数据项？').then(() => {
    return delServiceOrder(_orderIds)
  }).then(() => { getList(); proxy.$modal.msgSuccess("删除成功") }).catch(() => {})
}

function handleExport() {
  proxy.download('order/serviceOrder/export', { ...queryParams.value }, `serviceOrder_${new Date().getTime()}.xlsx`)
}

/** ====== 业务操作 ====== */

/** 指派员工 */
function handleAssign(row) {
  assignOrderId.value = row.orderId
  assignOrderNo.value = row.orderNo
  assignStaffId.value = null
  staffOptions.value = []
  assignOpen.value = true
}
function submitAssign() {
  if (!assignStaffId.value) { proxy.$modal.msgWarning("请选择服务人员"); return }
  assignLoading.value = true
  assignStaff({ orderId: assignOrderId.value, staffId: assignStaffId.value }).then(() => {
    proxy.$modal.msgSuccess("指派成功")
    assignOpen.value = false
    getList()
  }).finally(() => { assignLoading.value = false })
}

/** 开始服务 */
function handleStartService(row) {
  proxy.$modal.confirm('确认将订单 "' + row.orderNo + '" 标记为【服务中】？').then(() => {
    changeOrderStatus({ orderId: row.orderId, orderStatus: 2 }).then(() => {
      proxy.$modal.msgSuccess("已标记为服务中")
      getList()
    })
  }).catch(() => {})
}

/** 标记完成 */
function handleComplete(row) {
  proxy.$modal.confirm('确认将订单 "' + row.orderNo + '" 标记为【已完成】？').then(() => {
    changeOrderStatus({ orderId: row.orderId, orderStatus: 3 }).then(() => {
      proxy.$modal.msgSuccess("已标记为已完成")
      getList()
    })
  }).catch(() => {})
}

getList()
</script>
