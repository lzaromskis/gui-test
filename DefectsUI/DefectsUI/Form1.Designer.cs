using DefectsUI.Db;
using System.Data;

namespace DefectsUI
{
    partial class Form1
    {
        /// <summary>
        ///  Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        ///  Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        ///  Required method for Designer support - do not modify
        ///  the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.DefectsTable = new System.Windows.Forms.DataGridView();
            this.LoadingBar = new System.Windows.Forms.ProgressBar();
            this.BackgroundDataLoader = new System.ComponentModel.BackgroundWorker();
            this.LoadingLabel = new System.Windows.Forms.Label();
            ((System.ComponentModel.ISupportInitialize)(this.DefectsTable)).BeginInit();
            this.SuspendLayout();
            // 
            // DefectsTable
            // 
            this.DefectsTable.AllowUserToAddRows = false;
            this.DefectsTable.AllowUserToDeleteRows = false;
            this.DefectsTable.AllowUserToResizeRows = false;
            this.DefectsTable.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.DefectsTable.AutoSizeRowsMode = System.Windows.Forms.DataGridViewAutoSizeRowsMode.AllCells;
            this.DefectsTable.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.DefectsTable.Location = new System.Drawing.Point(12, 9);
            this.DefectsTable.Name = "DefectsTable";
            this.DefectsTable.ReadOnly = true;
            this.DefectsTable.RowTemplate.Height = 25;
            this.DefectsTable.Size = new System.Drawing.Size(834, 468);
            this.DefectsTable.TabIndex = 0;
            this.DefectsTable.Visible = false;
            this.DefectsTable.CellContentClick += new System.Windows.Forms.DataGridViewCellEventHandler(this.defectsTable_CellContentClick);
            // 
            // LoadingBar
            // 
            this.LoadingBar.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.LoadingBar.Location = new System.Drawing.Point(12, 447);
            this.LoadingBar.MarqueeAnimationSpeed = 16;
            this.LoadingBar.MaximumSize = new System.Drawing.Size(100000, 30);
            this.LoadingBar.Name = "LoadingBar";
            this.LoadingBar.Size = new System.Drawing.Size(834, 30);
            this.LoadingBar.Step = 5;
            this.LoadingBar.Style = System.Windows.Forms.ProgressBarStyle.Marquee;
            this.LoadingBar.TabIndex = 1;
            // 
            // BackgroundDataLoader
            // 
            this.BackgroundDataLoader.DoWork += new System.ComponentModel.DoWorkEventHandler(this.BackgroundDataLoader_DoWork);
            this.BackgroundDataLoader.RunWorkerCompleted += new System.ComponentModel.RunWorkerCompletedEventHandler(this.BackgroundDataLoader_RunWorkerCompleted);
            // 
            // LoadingLabel
            // 
            this.LoadingLabel.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.LoadingLabel.Font = new System.Drawing.Font("Segoe UI", 25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point);
            this.LoadingLabel.Location = new System.Drawing.Point(12, 358);
            this.LoadingLabel.MaximumSize = new System.Drawing.Size(100000, 50);
            this.LoadingLabel.Name = "LoadingLabel";
            this.LoadingLabel.Size = new System.Drawing.Size(834, 50);
            this.LoadingLabel.TabIndex = 2;
            this.LoadingLabel.Text = "Loading data...";
            this.LoadingLabel.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(7F, 15F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(858, 492);
            this.Controls.Add(this.DefectsTable);
            this.Controls.Add(this.LoadingBar);
            this.Controls.Add(this.LoadingLabel);
            this.Name = "Form1";
            this.Text = "DefectsUI";
            ((System.ComponentModel.ISupportInitialize)(this.DefectsTable)).EndInit();
            this.ResumeLayout(false);

        }

        #endregion

        protected override void OnCreateControl()
        {
            base.OnCreateControl();
            BackgroundDataLoader.RunWorkerAsync();
            //var dt = new DataTable();
            //var db = new DefectsDb();
            //db.Query(dt);
            //DefectsTable.DataSource = dt;
            //DefectsTable.AutoResizeColumn(DefectsTable.Columns.Count - 1, DataGridViewAutoSizeColumnMode.AllCells);
        }

        private DataGridView DefectsTable;
        private ProgressBar LoadingBar;
        private System.ComponentModel.BackgroundWorker BackgroundDataLoader;
        private Label LoadingLabel;
    }
}