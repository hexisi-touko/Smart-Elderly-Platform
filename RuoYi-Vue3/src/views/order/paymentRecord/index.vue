<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="关联服务订单ID" prop="orderId">
        <el-input
          v-model="queryParams.orderId"
          placeholder="请输入关联服务订单ID"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="支付时间" style="width: 308px">
        <el-date-picker
          v-model="daterangePaymentTime"
          value-format="YYYY-MM-DD"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="支付方式" prop="paymentMethod">
        <el-select v-model="queryParams.paymentMethod" placeholder="请选择支付方式" clearable>
          <el-option
            v-for="dict in pay_method"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="支付状态" prop="paymentStatus">
        <el-select v-model="queryParams.paymentStatus" placeholder="请选择支付状态" clearable>
          <el-option
            v-for="dict in pay_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="第三方支付流水号" prop="transactionId">
        <el-input
          v-model="queryParams.transactionId"
          placeholder="请输入第三方支付流水号"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="退款标记" prop="refundFlag">
        <el-input
          v-model="queryParams.refundFlag"
          placeholder="请输入退款标记"
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
          v-hasPermi="['order:paymentRecord:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['order:paymentRecord:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['order:paymentRecord:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['order:paymentRecord:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="paymentRecordList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="支付记录id" align="center" prop="paymentId" />
      <el-table-column label="关联服务订单ID" align="center" prop="orderId" />
      <el-table-column label="支付金额" align="center" prop="paymentAmount" />
      <el-table-column label="支付时间" align="center" prop="paymentTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.paymentTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="支付方式" align="center" prop="paymentMethod">
        <template #default="scope">
          <dict-tag :options="pay_method" :value="scope.row.paymentMethod"/>
        </template>
      </el-table-column>
      <el-table-column label="支付状态" align="center" prop="paymentStatus">
        <template #default="scope">
          <dict-tag :options="pay_status" :value="scope.row.paymentStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="第三方支付流水号" align="center" prop="transactionId" />
      <el-table-column label="退款标记" align="center" prop="refundFlag" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['order:paymentRecord:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['order:paymentRecord:remove']">删除</el-button>
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

    <!-- 添加或修改支付记录管理对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="paymentRecordRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="关联服务订单ID" prop="orderId">
          <el-input v-model="form.orderId" placeholder="请输入关联服务订单ID" />
        </el-form-item>
        <el-form-item label="支付金额" prop="paymentAmount">
          <el-input v-model="form.paymentAmount" placeholder="请输入支付金额" />
        </el-form-item>
        <el-form-item label="支付方式" prop="paymentMethod">
          <el-select v-model="form.paymentMethod" placeholder="请选择支付方式">
            <el-option
              v-for="dict in pay_method"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="支付状态" prop="paymentStatus">
          <el-select v-model="form.paymentStatus" placeholder="请选择支付状态">
            <el-option
              v-for="dict in pay_status"
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

<script setup name="PaymentRecord">
import { listPaymentRecord, getPaymentRecord, delPaymentRecord, addPaymentRecord, updatePaymentRecord } from "@/api/order/paymentRecord"

const { proxy } = getCurrentInstance()
const { pay_status, pay_method } = proxy.useDict('pay_status', 'pay_method')

const paymentRecordList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
const daterangePaymentTime = ref([])

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    orderId: null,
    paymentTime: null,
    paymentMethod: null,
    paymentStatus: null,
    transactionId: null,
    refundFlag: null,
  },
  rules: {
    orderId: [
      { required: true, message: "关联服务订单ID不能为空", trigger: "blur" }
    ],
    paymentAmount: [
      { required: true, message: "支付金额不能为空", trigger: "blur" }
    ],
    paymentTime: [
      { required: true, message: "支付时间不能为空", trigger: "blur" }
    ],
    paymentStatus: [
      { required: true, message: "支付状态不能为空", trigger: "change" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询支付记录管理列表 */
function getList() {
  loading.value = true
  queryParams.value.params = {}
  if (null != daterangePaymentTime.value && '' != daterangePaymentTime.value) {
    queryParams.value.params["beginPaymentTime"] = daterangePaymentTime.value[0]
    queryParams.value.params["endPaymentTime"] = daterangePaymentTime.value[1]
  }
  listPaymentRecord(queryParams.value).then(response => {
    paymentRecordList.value = response.rows
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
    paymentId: null,
    orderId: null,
    paymentAmount: null,
    paymentTime: null,
    paymentMethod: null,
    paymentStatus: null,
    transactionId: null,
    refundFlag: null,
    refundAmount: null,
    refundTime: null,
    refundReason: null,
    isDeleted: null,
    createTime: null,
    updateTime: null
  }
  proxy.resetForm("paymentRecordRef")
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  daterangePaymentTime.value = []
  proxy.resetForm("queryRef")
  handleQuery()
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.paymentId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加支付记录管理"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _paymentId = row.paymentId || ids.value
  getPaymentRecord(_paymentId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改支付记录管理"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["paymentRecordRef"].validate(valid => {
    if (valid) {
      if (form.value.paymentId != null) {
        updatePaymentRecord(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addPaymentRecord(form.value).then(response => {
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
  const _paymentIds = row.paymentId || ids.value
  proxy.$modal.confirm('是否确认删除支付记录管理编号为"' + _paymentIds + '"的数据项？').then(function() {
    return delPaymentRecord(_paymentIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('order/paymentRecord/export', {
    ...queryParams.value
  }, `paymentRecord_${new Date().getTime()}.xlsx`)
}

getList()
</script>
